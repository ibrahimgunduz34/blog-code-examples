<?php
namespace App\Controller;

use App\Message\EmailNotification;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class DefaultController extends AbstractController
{
    /**
     * @Route("/registration/complete")
     */
    public function completeRegistration()
    {
        $message = new EmailNotification('user@example.com', 'Welcome!', 'Registration has been completed.');
        $this->dispatchMessage($message);
        return new Response("Registration Complete!");
    }
}
