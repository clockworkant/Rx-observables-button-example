import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by alec on 07/10/2015.
 */
class View {

    private final PublishSubject<Object> buttonClickedSubject;
    private final PublishSubject<String> firstNameSubject;
    private final PublishSubject<String> lastNameSubject;

    View() {
        buttonClickedSubject = PublishSubject.create();
        firstNameSubject = PublishSubject.create();
        lastNameSubject = PublishSubject.create();
    }

    public Observable<Object> getButtonClickedObservable() {
        return buttonClickedSubject.doOnNext(o -> System.out.println("->Button Clicked"));
    }

    public Observable<String> getFirstNameObservable() {
        return firstNameSubject.doOnNext(firstName -> System.out.println("->First Name: " + firstName));
    }

    public Observable<String> getLastNameObservable() {
        return lastNameSubject.doOnNext(firstName -> System.out.println("->Last Name: " + firstName));
    }
}
