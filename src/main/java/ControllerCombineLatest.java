import rx.Observable;

public class ControllerCombineLatest implements Controller {

    private ServerService serverService;

    public ControllerCombineLatest(ServerService serverService) {
        this.serverService = serverService;
    }

    @Override
    public void initWith(View view){
        Observable<String> fullNameObservable = Observable
                .combineLatest(view.getFirstNameObservable(), view.getLastNameObservable(), (firstName, lastName) -> firstName + " " + lastName);

        Observable.combineLatest(fullNameObservable, view.getButtonClickedObservable(), (fullName, buttonClickedEvent) -> fullName)
                .subscribe(serverService::sendDataToServer);
    }

}
