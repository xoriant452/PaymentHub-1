# PaymentHub

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 6.2.3.

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `--prod` flag for a production build.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).

================================

--------  Payment Hub (Angular) ---------------------------------------------

prerequisites: node, npm and angular-cli must be installed on the system.
        1. npm is a package manager that comes with the nodejs installation.
        2. To install angular-cli use following command: npm install -g @angular/cli

1. Extract paymentHub-7-ui.zip
2. Once extracted,open cmd, and go to the folder path.
3. run the command npm install, this will install all the dependencies for the code.
4. Update the hostName const in hostName.ts file("src\app\shared\hostName.ts") with the server address of the Yogesh's code.
5. In the cmd use command ng serve, to run the angular app locally on port 4200.

---- Steps to deploy Angular app to server ---------------------
Once the app is build and run using above steps, to deploy tha angular app to the server following steps needs to be taken care of:

1. Build the app for production with the following command: ng build --prod --aot
2. The above will generate dist folder, which contents needs to be deployed on the server
3. Set the <base> element correctly in dist folder:
        In index.html file change base tag if our app is on other path.
        e.g: if app is at example.com/my-app then update, <base href="/"> to <base href="/my-app/">
4. Server side configuration: Make sure server always return index.html file as routes are in angular app and server wont know about it!

