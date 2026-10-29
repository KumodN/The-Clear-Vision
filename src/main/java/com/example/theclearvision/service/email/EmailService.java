package com.example.theclearvision.service.email;

import com.example.theclearvision.feature.appointment.Appointment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.name}")
    private String appName;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public void sendAppointmentConfirmation(Appointment appointment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(appointment.getUser().getEmail());
            helper.setSubject("Appointment Confirmation - " + appName);

            String emailContent = buildAppointmentEmail(appointment);
            helper.setText(emailContent, true);

            mailSender.send(message);
            log.info("Appointment confirmation email sent to: {}", appointment.getUser().getEmail());
        } catch (MessagingException e) {
            log.error("Failed to send appointment confirmation email", e);
            // Don't throw exception - email failure shouldn't break appointment creation
        }
    }

    private String buildAppointmentEmail(Appointment appointment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a");
        String formattedDateTime = appointment.getAppointmentTime().format(formatter);

        String qrCodeBase64 = generateQRCode(appointment);

        String doctorInfo = "";
        if (appointment.getDoctor() != null) {
            doctorInfo = String.format(
                    "<p><strong>Doctor:</strong> Dr. %s %s</p>",
                    appointment.getDoctor().getFirstName(),
                    appointment.getDoctor().getLastName());
            if (appointment.getDoctor().getSpecialization() != null) {
                doctorInfo += String.format(
                        "<p><strong>Specialization:</strong> %s</p>",
                        appointment.getDoctor().getSpecialization());
            }
        }

        return String.format(
                """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <style>
                                body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                                .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                                .header { background-color: #2563eb; color: white; padding: 20px; text-align: center; border-radius: 8px 8px 0 0; }
                                .content { background-color: #f9fafb; padding: 30px; border: 1px solid #e5e7eb; }
                                .details { background-color: white; padding: 20px; margin: 20px 0; border-radius: 8px; border-left: 4px solid #2563eb; }
                                .qr-code { text-align: center; margin: 20px 0; }
                                .footer { text-align: center; margin-top: 20px; font-size: 12px; color: #6b7280; }
                                .button { display: inline-block; background-color: #2563eb; color: white; padding: 12px 24px; text-decoration: none; border-radius: 6px; margin: 10px 0; }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <div class="header">
                                    <h1>%s</h1>
                                    <p>Appointment Confirmation</p>
                                </div>
                                <div class="content">
                                    <h2>Hello %s,</h2>
                                    <p>Your appointment has been successfully scheduled!</p>

                                    <div class="details">
                                        <h3>Appointment Details</h3>
                                        <p><strong>Appointment ID:</strong> #%d</p>
                                        <p><strong>Date & Time:</strong> %s</p>
                                        <p><strong>Branch:</strong> %s</p>
                                        %s
                                        <p><strong>Status:</strong> %s</p>
                                    </div>

                                    <div class="qr-code">
                                        <h3>Your Appointment QR Code</h3>
                                        <p>Show this at your appointment:</p>
                                        <img src="data:image/png;base64,%s" alt="Appointment QR Code" style="max-width: 200px;">
                                    </div>

                                    <p style="text-align: center;">
                                        <a href="%s/appointments" class="button">View My Appointments</a>
                                    </p>

                                    <p><strong>Important Notes:</strong></p>
                                    <ul>
                                        <li>You can cancel this appointment within 24 hours of booking</li>
                                        <li>Please arrive 10 minutes early</li>
                                        <li>Bring a valid ID</li>
                                    </ul>
                                </div>
                                <div class="footer">
                                    <p>This is an automated message. Please do not reply to this email.</p>
                                    <p>&copy; 2024 %s. All rights reserved.</p>
                                </div>
                            </div>
                        </body>
                        </html>
                        """,
                appName,
                appointment.getUser().getFirstName(),
                appointment.getId(),
                formattedDateTime,
                appointment.getBranch().getName(),
                doctorInfo,
                appointment.getStatus(),
                qrCodeBase64,
                frontendUrl,
                appName);
    }

    private String generateQRCode(Appointment appointment) {
        try {
            String qrContent = String.format(
                    "APPOINTMENT:%d|USER:%s|TIME:%s|BRANCH:%s",
                    appointment.getId(),
                    appointment.getUser().getEmail(),
                    appointment.getAppointmentTime(),
                    appointment.getBranch().getName());

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300);

            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();

            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (WriterException | IOException e) {
            log.error("Failed to generate QR code", e);
            return "";
        }
    }
}
