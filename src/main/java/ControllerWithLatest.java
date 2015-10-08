import rx.Observable;

public class ControllerWithLatest implements Controller{

    private ServerService serverService;

    public ControllerWithLatest(ServerService serverService) {
        this.serverService = serverService;
    }

    public void initWith(View view){
        Observable<String> fullNameObservable = Observable
                .combineLatest(view.getFirstNameObservable(), view.getLastNameObservable(), (firstName, lastName) -> firstName + " " + lastName);

        view.getButtonClickedObservable()
                .withLatestFrom(fullNameObservable, (clickEvent, fullName) -> fullName)
                .subscribe(serverService::sendDataToServer);
    }
}
