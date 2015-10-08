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

public class ControllerTest {
    private PublishSubject<Object> buttonClickedSubject;
    private PublishSubject<String> firstNameSubject;
    private PublishSubject<String> lastNameSubject;

    @Mock View view;
    @Spy ServerService serverService;

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.println("\ntest: " + description.getMethodName());
        }
    };

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        buttonClickedSubject = PublishSubject.create();
        firstNameSubject = PublishSubject.create();
        lastNameSubject = PublishSubject.create();

        when(view.getButtonClickedObservable()).thenReturn(buttonClickedSubject.doOnNext(o -> System.out.println("-> Button Clicked")));
        when(view.getFirstNameObservable()).thenReturn(firstNameSubject.doOnNext(firstName -> System.out.println("-> First Name: " + firstName)));
        when(view.getLastNameObservable()).thenReturn(lastNameSubject.doOnNext(firstName -> System.out.println("-> Last Name: " + firstName)));

        newController(serverService).initWith(view);
    }

    private Controller newController(ServerService serverService) {
        return new ControllerZipWith(serverService);
//        return new ControllerWithLatest(serverService);
//        return new ControllerCombineLatest(serverService);
    }

    @Test
    public void clickButtonBeforeContentReady_shouldDoNothing() throws Exception {

        buttonClickedSubject.onNext(new Object());

        firstNameSubject.onNext("Jon");
        firstNameSubject.onNext("Mike");

        lastNameSubject.onNext("Smith");
        lastNameSubject.onNext("Jones");

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