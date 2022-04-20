<?php
namespace App\MessageHandler;

use App\Message\EmailNotification;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Messenger\Handler\MessageHandlerInterface;
use Symfony\Component\Mime\Email;

class EmailNotificationHandler implements MessageHandlerInterface
{
    private $mailer;

    public function __construct(MailerInterface $mailer)
    {
        $this->mailer = $mailer;
    }

    public function __invoke(EmailNotification $message)
    {
        $email = (new Email())
            ->from('host@me.local')
            ->subject($message->getSubject())
            ->to($message->getRecipient())
            ->text($message->getContent());
        $this->mailer->send($email);
    }
}
