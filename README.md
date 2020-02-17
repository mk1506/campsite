## Upgrade Backend Coding Challenge

### Installation

run `gradle clean build`

### Running the application

from main folder run `java -jar ./build/libs/campsite-1.0-all.jar`

the application is ready when `application started` appears in the console

#### Using the rest service

The application can be reached at `localhost:5000` this will display a interactive swagger UI that can be used to test the api along with the details of all included resources.

#### Implementation notes

##### Data storage:
To make running this easier I am simply using a hashmap in memory (persistence of data between applications was not a constraint). In a real scenario some flavor of SQL would be good for this workload but I wanted to maintain the ability to run this without additional setup steps/complications.

#### Testing

I have included tests for the service layer covering all of the constraints presented in the provided document.

In a real production setting I would of course provide full test coverage and unit tests of all classes however to save time here I hope this is enough of a sample. If you wish to see full test coverage please reach out to me I can spend some more time to get it done.

Concerning the Tests for load/concurrency I have included the file `JMeter-tests` with 2 relevant test groups:

- Load Test Group: This sends 1000 concurrent requests to the server the *Summary Report* section will show the results of this test. I was able to run 1500+ requests with no errors. In a production environemnt I would for example run multiple replicas of this service behind a load balancer to increase capacities further if required.
- Concurrency Test Group: This sends 10 identical create reservation events. You will see from the results that only 1 request will be successful as expected. This is achieved in this simplistic setup via the synchronized keyword on operations that must be thread safe. 
   - Note you will have to update the date of the reservation for the requests in this test depending nn when you run it.