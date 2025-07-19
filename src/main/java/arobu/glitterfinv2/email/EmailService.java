package arobu.glitterfinv2.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final SesV2Client sesClient;

    @Value("${aws.ses.from.email}")
    private String fromEmail;

    @Value("${aws.ses.from.name}")
    private String fromName;

    public EmailService(SesV2Client sesClient) {
        this.sesClient = sesClient;
    }

    public boolean sendEmail(String toEmail, String subject, String textBody) {
        Destination destination = Destination.builder()
                .toAddresses(toEmail)
                .build();

        Content content = Content.builder()
                .data(textBody)
                .build();

        Content sub = Content.builder()
                .data(subject)
                .build();

        Body body = Body.builder()
                .html(content)
                .build();

        Message msg = Message.builder()
                .subject(sub)
                .body(body)
                .build();

        EmailContent emailContent = EmailContent.builder()
                .simple(msg)
                .build();

        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .destination(destination)
                .content(emailContent)
                .fromEmailAddress(fromEmail)
                .build();

        try {
            logger.info("Attempting to send an email through Amazon SES "
                    + "using the AWS SDK for Java...");
            sesClient.sendEmail(emailRequest);
            logger.info("email was sent");

        } catch (SesV2Exception e) {
            logger.error(e.awsErrorDetails().errorMessage());
        }

        return true;
    }

}
