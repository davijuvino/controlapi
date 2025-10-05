package br.com.controlapi.service;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Locale;

import br.com.controlapi.model.entity.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.internet.MimeMessage;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
public class EmailService {

    private final Logger log = LoggerFactory.getLogger(EmailService.class);

    private static final String USUARIO = "u";
    private static final String BASE_URL = "baseUrl";
    private static final String DATA_EVENTO = "dataEvento";

    private final MailProperties mailProperties;
    private final JavaMailSender javaMailSender;
    private final MessageSource messageSource;
    private final SpringTemplateEngine templateEngine;

    public EmailService(
            MailProperties mailProperties,
            JavaMailSender javaMailSender,
            MessageSource messageSource,
            SpringTemplateEngine templateEngine
    ) {
        this.mailProperties = mailProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());

            message.setTo(to);
            message.setFrom(mailProperties.getUsername()); // Ou use um email específico configurado
            message.setSubject(subject);
            message.setText(content, isHtml);

            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            log.debug("Email sending error details", e);
        }
    }

    public void sendEmailFromTemplate(Usuario usuario, String templateName, String titleKey) {
        if (usuario.getEmail() == null) {
            log.warn("Email address is null for user {}", usuario.getLogin());
            return;
        }

        Locale locale = Locale.forLanguageTag(usuario.getLangChave() != null ? usuario.getLangChave() : "pt");
        Context context = new Context(locale);
        context.setVariable(USUARIO, usuario);
        context.setVariable(BASE_URL, getBaseUrl());
        context.setVariable(DATA_EVENTO, ZonedDateTime.now());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);

        sendEmail(usuario.getEmail(), subject, content, false, true);
    }

    private String getBaseUrl() {
        // Você pode configurar isso nas propriedades do Spring
        // Por exemplo: spring.mail.base-url=http://localhost:8080
        return mailProperties.getProperties().getOrDefault("base-url", "http://localhost:8080");
    }

    @Async("emailExecutor")
    public void enviarEmailDeAtivacao(Usuario usuario) {
        log.debug("Sending activation email to '{}'", usuario.getEmail());
        sendEmailFromTemplate(usuario, "mail/activationEmail", "email.activation.title");
    }

    @Async("emailExecutor")
    public void sendCreationEmail(Usuario usuario) {
        log.debug("Sending creation email to '{}'", usuario.getEmail());
        sendEmailFromTemplate(usuario, "mail/creationEmail", "email.activation.title");
    }

    @Async("emailExecutor")
    public void sendPasswordResetMail(Usuario usuario) {
        log.debug("Sending password reset email to '{}'", usuario.getEmail());
        sendEmailFromTemplate(usuario, "mail/passwordResetEmail", "email.reset.title");
    }
}
