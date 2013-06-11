/**
 * Copyright 2013 Peergreen S.A.S.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.peergreen.webconsole.pocpush;

import javax.servlet.ServletException;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import com.vaadin.server.UIProvider;

@Component
@Instantiate
public class MyConsole {

    @Requires
    private HttpService httpService;

    @Requires
    private UIProvider uiProvider;

    @Validate
    public void start() throws ServletException, NamespaceException {
        VaadinOSGiServlet servlet = new VaadinOSGiServlet(uiProvider);
        httpService.registerServlet("/pocpush", servlet,null, null);
        System.out.println("Poc available on /pocpush");
    }


    @Invalidate
    public void stop() {
        httpService.unregister("/pocpush");
    }


}
