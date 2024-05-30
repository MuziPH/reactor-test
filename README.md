Project Reactor
- Publisher = Supplier
- Subscriber = Consumer
- Subscription
- Processor = Function
  
* When the Subcriber subcribes to the publisher and it is passed to the onSubcribe method.
* The Subscriber requests a number of elements.
* The Publisher then starts sending them.
* When the Publisher has no more elements to send, onComplete method is called.
* If there is an error the Subscription is cancelled, and onError method called.
