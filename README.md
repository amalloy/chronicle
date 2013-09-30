# chronicle

Chronicle is a dead simple library for getting an infinite sequence of times
that match a certain specification. It is essentially an abstract version of
cron, using Clojure maps to represent time specifications. Chronicle makes use
of clj-time and Joda-time and returns Joda time objects.

## Usage

Chronicle uses Clojure maps as time specifications. Here is the default spec:

```clojure
{:minute (range 60)
 :hour (range 24)
 :day (range 1 32)
 :month (range 1 13)
 :day-of-week (range 1 8)}
```

This default spec will get you an infinite seq of times for every minute from
now until the heat death of the universe. It reads like this:

Find all times that have the following attributes: every minute (0 -> 59),
every hour (0 -> 24), every day (1 -> 31), every month (1 -> 12), and every day
of the week (1 -> 7).

This default specification will always be the base spec, and the spec that you
pass to Chronicle will be merged in, thus overriding the parts that you change.

Let's try getting times for every 5 minutes:

```clojure
user=> (require '[flatland.chronicle :as c])
nil
user=> (require '[clj-time.core :as t])
nil
user=> (take 10 (c/times-for {:minute (range 0 60 5)} (t/now)))
(#<DateTime 2013-09-30T21:15:00.000Z> #<DateTime 2013-09-30T21:20:00.000Z> #<DateTime 2013-09-30T21:25:00.000Z> #<DateTime 2013-09-30T21:30:00.000Z> #<DateTime 2013-09-30T21:35:00.000Z> #<DateTime 2013-09-30T21:40:00.000Z> #<DateTime 2013-09-30T21:45:00.000Z> #<DateTime 2013-09-30T21:50:00.000Z> #<DateTime 2013-09-30T21:55:00.000Z> #<DateTime 2013-09-30T22:00:00.000Z>)
```

That's pretty much it. Pretty simple, eh?

## License

Distributed under the Eclipse Public License, the same as Clojure.
