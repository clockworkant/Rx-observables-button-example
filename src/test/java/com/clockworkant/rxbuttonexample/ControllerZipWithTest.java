package com.clockworkant.rxbuttonexample;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import rx.subjects.PublishSubject;

import static org.mockito.Mockito.*;

public class ControllerZipWithTest {
    private final PublishSubject<Object> buttonClickedSubject = PublishSubject.create();
    private final PublishSubject<String> firstNameSubject = PublishSubject.create();
    private final PublishSubject<String> lastNameSubject = PublishSubject.create();

    @Mock
    View view;
    @Spy
    ServerService serverService;

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.println("\n" + ControllerZipWithTest.class.getSimpleName() + ": " + description.getMethodName());
        }
    };

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        when(view.getButtonClickedObservable()).thenReturn(buttonClickedSubject.doOnNext(o -> System.out.println("-> Button Clicked")));
        when(view.getFirstNameObservable()).thenReturn(firstNameSubject.doOnNext(firstName -> System.out.println("-> First Name: " + firstName)));
        when(view.getLastNameObservable()).thenReturn(lastNameSubject.doOnNext(firstName -> System.out.println("-> Last Name: " + firstName)));

        Controller controller = new ControllerZipWith(serverService);
        controller.initWith(view);
    }

    /**
     * Expected to fail
     * In order to pair our click event to our name being published we attempt to pair the items.
     * The button is clicked and waits for a name to pair with.
     * A name is emitted and immediately sends the data to the server as the button now has a paired item
     * @throws Exception
     */
    @Test
    public void clickButtonBeforeContentReady_shouldDoNothing() throws Exception {

        buttonClickedSubject.onNext(new Object());

        firstNameSubject.onNext("Jon");
        lastNameSubject.onNext("Jones");

        firstNameSubject.onNext("Mike");
        lastNameSubject.onNext("Mills");

        verify(serverService, never()).sendDataToServer(any(String.class));
    }

    @Test
    public void provideContentThenClickButton_shouldSendDataCurrentNameToServer() throws Exception {

        firstNameSubject.onNext("Jon");
        lastNameSubject.onNext("Jones");

        buttonClickedSubject.onNext(new Object());

        verify(serverService).sendDataToServer("Jon Jones");
    }
}