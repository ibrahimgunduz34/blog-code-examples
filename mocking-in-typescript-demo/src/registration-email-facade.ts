import { SmtpEmailTransporter } from './smtp-email-transporter'
import { TemplateEngine } from './template-engine'
import { Logger } from './logger'
import { User } from './user'

export class RegistrationEmailFacade {
  constructor (
    private readonly emailTransporter: SmtpEmailTransporter,
    private readonly templateEngine: TemplateEngine,
    private readonly logger: Logger,
  ) {}
  
  public send(user: User): void {
    const content = this.templateEngine.render('registration', user);
    try {
      this.emailTransporter.send(user.email, content);
    } catch(error) {
      this.logger.error(error);
      throw new Error('Registration email could not send');
    }
  }
}