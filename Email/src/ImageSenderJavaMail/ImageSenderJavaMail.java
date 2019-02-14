package ImageSenderJavaMail;

/**
  * @author João Henri e Caio Rosa
**/

import java.util.*;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage;

public class ImageSenderJavaMail {

  public static void main (String args[]) {
    try {
      String host ="smtp.gmail.com";
      String user = "littleheadfilms@gmail.com";
      String pass = "EmailLH12";
      String to = "joao.henri.cr@gmail.com";
      String from = "littleheadfilms@gmail.com";
      String subject = "JavaMail Project";
      String messageText = "Este e-mail foi enviado utilizado a API JavaMail";
      boolean sessionDebug = false;

      Properties props = System.getProperties();

      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.port", "587");
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.required", "true");

      java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
      Session mailSession = Session.getDefaultInstance(props, null);
      mailSession.setDebug(sessionDebug);
      Message msg = new MimeMessage(mailSession);
      msg.setFrom(new InternetAddress(from));
      InternetAddress[] address = {new InternetAddress(to)};
      msg.setRecipients(Message.RecipientType.TO, address);
      msg.setSubject(subject); msg.setSentDate(new Date());
      msg.setText(messageText);

      // O Multipart possui duas partes principais: As tags HTML e a própria imagem.
      MimeMultipart multipart = new MimeMultipart("related");

      // Colocar as tags HTML em que a imagem será carregada
      BodyPart messageBodyPart = new MimeBodyPart();
      String htmlText = " <p>Este e-mail foi enviado utilizado a API JavaMail. </p> <H1>Segue foto anexada: </H1><img src=\"cid:image\" width=900>";
      messageBodyPart.setContent(htmlText, "text/html");
      // add it
      multipart.addBodyPart(messageBodyPart);

      // Segunda parte: Imagem
      messageBodyPart = new MimeBodyPart();
      DataSource fds = new FileDataSource(
      "moon.png");

      messageBodyPart.setDataHandler(new DataHandler(fds));
      messageBodyPart.setHeader("Content-ID", "<image>");

      // Colocar imagem no corpo do Multipart
      multipart.addBodyPart(messageBodyPart);

      // Juntar Message + Multipart
      msg.setContent(multipart);

      Transport transport = mailSession.getTransport("smtp");
      transport.connect(host, user, pass);
      transport.sendMessage(msg, msg.getAllRecipients());
      transport.close();
      System.out.println("E-mail enviado com sucesso!");
    } catch (MessagingException e) {
      System.out.println("Não foi possível enviar o e-mail pois ocorreu algum erro!");
      throw new RuntimeException(e);
    }
  }
}
