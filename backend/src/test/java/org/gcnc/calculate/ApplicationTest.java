package org.gcnc.calculate;


import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

public class ApplicationTest {
    @Mock
    RemoteWebDriver webDriver;

    @Before
    public void init() {

    }

    @Test
    public void main() throws UnreachableBrowserException {
        Assertions.catchThrowable(() ->  Application.main(new String[] {}));
    }
}