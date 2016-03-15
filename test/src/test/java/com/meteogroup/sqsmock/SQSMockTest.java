/*
Copyright © 2016 MeteoGroup Deutschland GmbH

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/
package com.meteogroup.sqsmock;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.fest.assertions.Assertions.assertThat;

public class SQSMockTest {

    public static final String QUEUE_NAME = "test-queue";
    public static final String MESSAGE_DATA = "message data";
    private AmazonSQSClient client;
    private String sqsmock;
    private String queueUrl;
    private String messageId;
    private String receiptHandle;

    @BeforeMethod
    public void setUp() throws Exception {
        sqsmock = System.getProperty("sqsmock");
        sqsmock = sqsmock != null ? sqsmock : "http://127.0.0.1:9324";
        client = new AmazonSQSClient();
        client.setEndpoint(sqsmock);
    }

    @Test
    public void create_queue() throws Exception {

        final CreateQueueResult createQueueResult = client.createQueue(QUEUE_NAME);

        queueUrl = createQueueResult.getQueueUrl();
        assertThat(queueUrl).startsWith(sqsmock).endsWith(QUEUE_NAME);
    }

    @Test(dependsOnMethods = "create_queue")
    public void list_queue() throws Exception {

        final ListQueuesResult result = client.listQueues();

        assertThat(result.getQueueUrls()).contains(queueUrl);
    }

    @Test(dependsOnMethods = "list_queue")
    public void delete_queue() throws Exception {

        client.deleteQueue(queueUrl);

        assertThat(client.listQueues().getQueueUrls()).excludes(queueUrl);
    }

    @Test(dependsOnMethods = "delete_queue")
    public void prepare_queue() throws Exception {

        queueUrl = client.createQueue(QUEUE_NAME).getQueueUrl();

        assertThat(queueUrl).startsWith(sqsmock).endsWith(QUEUE_NAME);
        assertThat(client.listQueues().getQueueUrls()).contains(queueUrl);
        assertThat(client.receiveMessage(new ReceiveMessageRequest(queueUrl).withMaxNumberOfMessages(10)).getMessages()).isEmpty();
    }

    @Test(dependsOnMethods = "prepare_queue")
    public void send_message() throws Exception {

        final SendMessageResult result = client.sendMessage(queueUrl, MESSAGE_DATA);

        messageId = result.getMessageId();
        assertThat(messageId).isNotEmpty();
    }

    @Test(dependsOnMethods = "send_message")
    public void receive_message() throws Exception {

        final ReceiveMessageResult result = client.receiveMessage(queueUrl);

        assertThat(result.getMessages()).isNotEmpty();
        final Message message = result.getMessages().get(0);
        assertThat(message.getMessageId()).isEqualTo(messageId);
        assertThat(message.getBody()).isEqualTo(MESSAGE_DATA);
        receiptHandle = message.getReceiptHandle();
        assertThat(receiptHandle).isNotEmpty();
    }

    @Test(dependsOnMethods = "receive_message")
    public void delete_message() throws Exception {

        client.deleteMessage(queueUrl, receiptHandle);

        // no exception
    }

    @AfterClass
    public void tearDown() throws Exception {
        if (queueUrl != null) {
            client.deleteQueue(queueUrl);
        }
    }
}
