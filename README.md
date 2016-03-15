
SQS mock
========

Docker image providing a SQS mock based on
[ElasticMQ](https://github.com/adamw/elasticmq):

> ElasticMQ is a message queue system, offering an actor-based Scala and
> an SQS-compatible REST (query) interface.

`test` contains a java test suite showing the use of _SQS mock_ together
with Amazon's [AWS SDK for Java ](https://aws.amazon.com/sdk-for-java/).

To build the image run `./build.sh`

To start the _SQS mock_ in a docker container run `./run.sh`.

By default _ElasticMQ_ is bound to port 9324. To select another port set
the `ELASTICMQ_PORT` environment variable, e.g.:

```bash
ELASTICMQ_PORT=1234 ./run.sh
```

If your docker host is not `localhost` (e.g. you are using docker-maching)
you have to set the visible server address by setting the `ELASTICMQ_HOST`
environment variable, e.g.:

```bash
ELASTICMQ_HOST=192.168.99.100 ./run.sh
```

To run the tests run `mvn test` within the `test` directory. Per default
tests try to connect to `http://127.0.0.1:9324`. This can be changed be
setting the `sqsmock` system property, e.g.:

```bash
mvn test -Dsqsmock=http://192.168.99.100:1234
```

Copyright & Licences
--------------------

*ElasticMQ* (http://elasticmq.org) © 2011-2012 SoftwareMill and Adam Warski,
licensed under the [Apache License 2.0](http://www.apache.org/licenses/).

*AWS SDK for Java* (https://aws.amazon.com/sdk-for-java/) © 2010-2014 Amazon.com, Inc.,
licensed under the [Apache License 2.0](http://www.apache.org/licenses/).

All files in this repository are released under the terms of the MIT License:

> Copyright © 2016 MeteoGroup Deutschland GmbH
>
> Permission is hereby granted, free of charge, to any person obtaining a copy
> of this software and associated documentation files (the "Software"), to deal
> in the Software without restriction, including without limitation the rights
> to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
> copies of the Software, and to permit persons to whom the Software is
> furnished to do so, subject to the following conditions:
>
> The above copyright notice and this permission notice shall be included in
> all copies or substantial portions of the Software.
>
> THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
> IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
> FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
> AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
> LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
> OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
> SOFTWARE.
