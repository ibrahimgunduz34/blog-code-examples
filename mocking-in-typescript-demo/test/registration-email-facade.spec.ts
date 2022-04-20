import { mock, instance, when, verify, anyString, anyOfClass, anything, deepEqual } from 'ts-mockito'

import { TemplateEngine } from '../src/template-engine'
import { SmtpEmailTransporter } from "../src/smtp-email-transporter"
import { RegistrationEmailFacade } from "../src/registration-email-facade"
import { Logger } from '../src/logger'
import { User } from '../src/user'

describe('RegistrationEmailFacade Test', () => {
  describe('When the send() method called - Email transporter works successfully', () => {
    it('should send a rendered email message successfully', () => {
        const mockedEmailTransporter: SmtpEmailTransporter = mock(SmtpEmailTransporter)
    
        const mockedTemplateEngine: TemplateEngine = mock(TemplateEngine)
        when(mockedTemplateEngine.render(anyString(), anyOfClass(User))).thenReturn('rendered content')

        const mockedLogger: Logger = mock(Logger)
        
        const user = new User("John Doe", "johndoe@test.net");

        const facade = new RegistrationEmailFacade(
            instance(mockedEmailTransporter), 
            instance(mockedTemplateEngine),
            instance(mockedLogger),
        )
        
        facade.send(user)
        
        verify(mockedTemplateEngine.render("registration", user)).once()
        verify(mockedEmailTransporter.send('johndoe@test.net', 'rendered content')).once()
        verify(mockedLogger.error(anything())).never()
    })
  })

  describe('When the send() method called - Email transporter fails with an error', () => {
    it('should fail with an error and log the error coming from the email transporter', () => {
        const mockedEmailTransporter: SmtpEmailTransporter = mock(SmtpEmailTransporter)
        const expectedError = new Error('Fail to connect to the SMTP server')
        when(mockedEmailTransporter.send(anyString(), anyString())).thenThrow(expectedError)

        const mockedTemplateEngine: TemplateEngine = mock(TemplateEngine)
        when(mockedTemplateEngine.render(anyString(), anyOfClass(User))).thenReturn('rendered content')

        const mockedLogger: Logger = mock(Logger)
        
        const user = new User("John Doe", "johndoe@test.net");

        const facade = new RegistrationEmailFacade(
            instance(mockedEmailTransporter), 
            instance(mockedTemplateEngine),
            instance(mockedLogger),
        )
        
        try {
            facade.send(user)
        } catch (error) {
            verify(mockedTemplateEngine.render("registration", user)).once()
            verify(mockedEmailTransporter.send('johndoe@test.net', 'rendered content')).once()
            verify(mockedLogger.error(expectedError)).once()
        }
    })
  })
})