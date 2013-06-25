package com.peergreen.webconsole.it;

import com.peergreen.deployment.Artifact;
import com.peergreen.deployment.ArtifactBuilder;
import com.peergreen.deployment.ArtifactProcessRequest;
import com.peergreen.deployment.DeploymentService;
import org.apache.felix.ipojo.architecture.Architecture;
import org.apache.felix.ipojo.extender.queue.QueueService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.ops4j.pax.exam.util.Filter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import javax.inject.Inject;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Mohammed Boukada
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CheckCreateConsole {

    private final static int NB_SESSIONS = 3;
    private final static String WEB_CONSOLE_URL = "http://localhost:9000/pgadmin/";

    @Inject
    @Filter("(ipojo.queue.mode=async)")
    private QueueService queueService;

    @Inject
    private BundleContext bundleContext;

    private StabilityHelper helper;

    @Inject
    private DeploymentService deploymentService;

    @Inject
    private ArtifactBuilder artifactBuilder;

    @Before
    public void init() {
        helper = new StabilityHelper(queueService);
    }

    @Test
    public void checkInject() {
        Assert.assertNotNull(bundleContext);
        Assert.assertNotNull(deploymentService);
        Assert.assertNotNull(artifactBuilder);
    }

    @Test
    public void testCreateConsole() throws Exception {
        URI fileURI = new URI("mvn:com.peergreen.webconsole/web-console-pgadmin/0.0.1-SNAPSHOT");
        Artifact webConsoleAdmin = artifactBuilder.build("web-console-admin.jar", fileURI);
        ArtifactProcessRequest artifactProcessRequest = new ArtifactProcessRequest(webConsoleAdmin);
        deploymentService.process(Collections.singleton(artifactProcessRequest));

        helper.waitForIPOJOStability();
        Thread.sleep(5000);

        Collection<ServiceReference<Architecture>> pgadmin =
                bundleContext.getServiceReferences(Architecture.class, "(architecture.instance=com.peergreen.webconsole.pgadmin.PeergreenAdminConsole-0)");
        Assert.assertTrue("Peergreen admin console was not deployed", pgadmin.size() == 1);

        Collection<ServiceReference<Architecture>> uiProvider =
                bundleContext.getServiceReferences(Architecture.class, "(architecture.instance=com.peergreen.webconsole.core.vaadin7.UIProviderBase-0)");
        Assert.assertTrue("One UIProvider for one console", uiProvider.size() == 1);

        Collection<ServiceReference<Architecture>> baseui;
        for (int i=0; i < NB_SESSIONS; i++) {
            helper.waitForWebConsoleStability(WEB_CONSOLE_URL);
            helper.waitForIPOJOStability();

            baseui = bundleContext.getServiceReferences(Architecture.class,
                    "(architecture.instance=com.peergreen.webconsole.core.vaadin7.BaseUI-" + i + ")");
            Assert.assertTrue("Console UI component for session " + i + " not created", baseui.size() == 1);
        }
    }
}
