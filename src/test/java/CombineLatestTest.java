import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rx.subjects.PublishSubject;

import static org.mockito.Mockito.*;

public class CombineLatestTest {

    private PublishSubject<Object> buttonClickedSubject;
    private PublishSubject<String> firstNameSubject;
    private PublishSubject<String> lastNameSubject;

    @Mock View view;
    @Mock ServerService serverService;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        buttonClickedSubject = PublishSubject.create();
        firstNameSubject = PublishSubject.create();
        lastNameSubject = PublishSubject.create();

        when(view.getButtonClickedObservable()).thenReturn(buttonClickedSubject.doOnNext(o -> System.out.println("-> Button Clicked")));
        when(view.getFirstNameObservable()).thenReturn(firstNameSubject.doOnNext(firstName -> System.out.println("-> First Name: " + firstName)));
        when(view.getLastNameObservable()).thenReturn(lastNameSubject.doOnNext(firstName -> System.out.println("-> Last Name: " + firstName)));
    }

    @Test
    public void testCombineLatestController_ClickButtonBeforeContentReady_shouldDoNothing() throws Exception {
        ControllerCombineLatest controller = new ControllerCombineLatest(serverService);
        controller.initWith(view);

        clickButtonThenUpdateName();

        verify(serverService, never()).sendDataToServer(any(String.class));
    }

    private void clickButtonThenUpdateName() {
        buttonClickedSubject.onNext(new Object());

        firstNameSubject.onNext("Jon");
        firstNameSubject.onNext("Mike");

        lastNameSubject.onNext("Smith");
        lastNameSubject.onNext("Jones");
    }
}