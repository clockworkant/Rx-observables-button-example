package com.clockworkant.rxbuttonexample;

import rx.Observable;

public class ControllerZipWith implements Controller {

    private ServerService serverService;

    public ControllerZipWith(ServerService serverService) {
        this.serverService = serverService;
    }

    @Override
    public void initWith(View view){
        Observable<String> fullNameObservable = Observable
                .combineLatest(
                        view.getFirstNameObservable(),
                        view.getLastNameObservable(),
                        (firstName, lastName) -> firstName + " " + lastName);

        //Each time the button is clicked, wait for a name to be published.
        view.getButtonClickedObservable()
                .zipWith(fullNameObservable, (o, fullName) -> fullName)
                .subscribe(serverService::sendDataToServer);
    }

}
