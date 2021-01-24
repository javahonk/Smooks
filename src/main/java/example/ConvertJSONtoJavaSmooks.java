package example;

import org.milyn.Smooks;
import org.milyn.SmooksException;
import org.milyn.container.ExecutionContext;
import org.milyn.io.StreamUtils;
import org.milyn.payload.StringResult;
import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * @author Javahonk 1/23/2021 9:31 PM
 */
public class ConvertJSONtoJavaSmooks {

    private static byte[] messageIn = readInputMessage("input-message.jsn");

    private final Smooks smooks;

    protected ConvertJSONtoJavaSmooks() throws IOException, SAXException {
        // Instantiate Smooks with the config...
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream("smooks-config.xml");
        smooks = new Smooks(inputStream);
    }

    protected String runSmooksTransform(ExecutionContext executionContext) throws IOException, SAXException, SmooksException {
        try {
            Locale defaultLocale = Locale.getDefault();
            Locale.setDefault(new Locale("en", "IE"));
            StringResult result = new StringResult();
            smooks.filterSource(executionContext, new StreamSource(new ByteArrayInputStream(messageIn)), result);
            Locale.setDefault(defaultLocale);
            return result.toString();
        } finally {
            smooks.close();
        }
    }

    public static void main(String[] args) throws IOException, SAXException, SmooksException {
        System.out.println("\n\n==============JOSN Message In==============");
        System.out.println(new String(messageIn));
        System.out.println("======================================\n");

        System.out.println("\n\n==============Smooks Message In==============");
        System.out.println(new String(readInputMessage("smooks-config.xml")));
        System.out.println("======================================\n");

        ConvertJSONtoJavaSmooks smooksMain = new ConvertJSONtoJavaSmooks();
        for (int i =0;i<10000;i++){
            long start  = System.currentTimeMillis();

            ExecutionContext executionContext = smooksMain.smooks.createExecutionContext();
            smooksMain.runSmooksTransform(executionContext);
            executionContext.getBeanContext().getBean("order");
            long end= System.currentTimeMillis();
            System.out.println(end-start);
        }
        ExecutionContext executionContext = smooksMain.smooks.createExecutionContext();
        smooksMain.runSmooksTransform(executionContext);
        System.out.println("==============JSON as Java Object Graph=============");
        System.out.println(executionContext.getBeanContext().getBean("order"));
        System.out.println("======================================\n\n");
    }

    private static byte[] readInputMessage(String path) {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classloader.getResourceAsStream(path);
            return StreamUtils.readStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return "<no-message/>".getBytes();
        }
    }
}
