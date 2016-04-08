package org.anc.togo

import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Transport
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

import org.anc.togo.constants.Globals

import org.slf4j.Logger
import org.slf4j.LoggerFactory;


/**
 * Based on code from http://www.javacommerce.com/destination65673/18274/SendMailUsingAuthentication.java
 * @author jestuart
 *
 */
class EmailSender {

   private final Logger logger = LoggerFactory.getLogger(EmailSender.class);

   public EmailSender() {
   }

      /**
       *
       * @param to
       * @param subject
       * @param message
       * @param from
       * @throws MessagingException
       */
      public void postMail(String to, String subject, String message) throws MessagingException {
         logger.info("Posting mail to {}", to)
         boolean debug = true

         //Set the host smtp address
         Properties props = new Properties()
         props.put("mail.smtp.host", Globals.SMTP.HOST)
         props.put("mail.smtp.auth", "true")

         Authenticator auth = new SMTPAuthenticator()
         Session session = Session.getDefaultInstance(props, auth)

         session.setDebug(debug)

         // create a message
         Message msg = new MimeMessage(session)

         // set the from and to address
         InternetAddress addressFrom = new InternetAddress(Globals.SMTP.FROM)
         msg.setFrom(addressFrom)

         InternetAddress addressTo = new InternetAddress(to)
         msg.setRecipient(Message.RecipientType.TO, addressTo)

         // Setting the Subject and Content Type
         msg.setSubject(subject)
         msg.setContent(message, "text/plain")
         try {
            logger.debug("Attempting to send mail.")
            Transport.send(msg)
            logger.info("Mail sent.");
         }
         catch (Exception e) {
            // TODO: handle exception
            logger.error("Error sending email.", e)
         }
      }
   }

   /**
    * SimpleAuthenticator is used to do simple authentication
    * when the SMTP server requires it.
    */
   public class SMTPAuthenticator extends Authenticator {
      public PasswordAuthentication getPasswordAuthentication() {
         String username = Globals.SMTP.USER
         String password = Globals.SMTP.PASSWORD
         return new PasswordAuthentication(username, password)
      }
   }



