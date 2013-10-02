import client.gui.AdministratorGUIPanel;
import org.junit.Test;
import server.exceptions.EntityUpdateException;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 10/2/13
 * Time: 12:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class OtherTests {
    @Test(expected = EntityUpdateException.class)
    public void routeInfoHandlerNegativeTest() throws Throwable {
        String route = "278\nПсков\nМосква_22:00";

        AdministratorGUIPanel targetObject = new AdministratorGUIPanel();
        Method method = AdministratorGUIPanel.class.getDeclaredMethod("routeInfoHandler", String.class);
        method.setAccessible(true);
        try {
            method.invoke(targetObject, route);
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }
}
