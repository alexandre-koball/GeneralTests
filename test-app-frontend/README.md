# test-app-frontend

## Description

It's the frontend portion of the Test App. It consists of a web interface that permits adding and retrieving purchase transactions from the backend portion of the application.

## How to build

To build it, run the following command from the console, from its base folder (prerequisite: having npm and Node.js installed):

        npm run build

If you encounter the following error:

        'react-scripts' is not recognized as an internal or external command

Please install the required module with the following command:

        npm install react-scripts --save

## How to run

To run the frontend, you should have "serve" installed. If that's not the case, install it with:

        npm install -g serve

Then, finally, run the application by running the following command from its base folder:

        serve -s build

The application will be available at the following address:

        http://localhost:3000
