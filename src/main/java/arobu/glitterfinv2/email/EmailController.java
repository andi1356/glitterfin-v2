package arobu.glitterfinv2.email;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/test")
    public String sendTestEmail() {
        boolean sent = emailService.sendEmail("arobuservices@gmail.com", "Test Email from Glitterfin", "this is a body test");
        return sent ? "Test email sent successfully!" : "Failed to send test email.";
    }
}
