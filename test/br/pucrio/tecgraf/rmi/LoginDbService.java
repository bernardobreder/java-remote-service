package br.pucrio.tecgraf.rmi;

import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;

import javax.security.auth.Subject;

/**
 * 
 * 
 * @author Tecgraf
 */
public class LoginDbService implements LoginService {

  /**
   * {@inheritDoc}
   */
  @Override
  public String login(String username, String password) throws RemoteException {
    //    return RmiUserContext.get().getString("session");
    Subject subject = new Subject();
    subject.getPrincipals().add(new Principal() {
      @Override
      public String getName() {
        return "bbreder";
      }
    });
    Subject.doAsPrivileged(subject, new PrivilegedAction<Void>() {
      @Override
      public Void run() {
        Subject user = Subject.getSubject(AccessController.getContext());
        System.out.println(".");
        return null;
      }
    }, AccessController.getContext());

    Subject user = Subject.getSubject(AccessController.getContext());
    return "ae";
  }

}
