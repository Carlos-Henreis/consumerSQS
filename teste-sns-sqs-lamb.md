### Mensagem no tópico SNS
Quando uma mensagem é publicada num tópico SNS, o corpo da mensagem (`Message`) é geralmente uma string JSON:

```json
{
  "default": "{\"teste\": \"banana\"}"
}
```

### Mensagem na fila SQS
Quando o SNS envia a mensagem para a fila SQS, a estrutura da mensagem fica um pouco mais complexa, pois a SQS adiciona metadados em torno do corpo da mensagem SNS:

```json
{
  "Type": "Notification",
  "MessageId": "12345678-1234-1234-1234-123456789012",
  "TopicArn": "arn:aws:sns:us-east-1:123456789012:MyTopic",
  "Subject": "MySubject",
  "Message": "{\"teste\": \"banana\"}",
  "Timestamp": "2023-06-18T12:00:00.000Z",
  "SignatureVersion": "1",
  "Signature": "EXAMPLE",
  "SigningCertURL": "https://sns.us-east-1.amazonaws.com/SimpleNotificationService-12345678901234567890.pem",
  "UnsubscribeURL": "https://sns.us-east-1.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:us-east-1:123456789012:MyTopic:2bcfbf39-05c3-41de-beaa-fcfcc21c8f55",
  "MessageAttributes": {}
}
```

### Evento processado pela Lambda
Quando a função Lambda é acionada pela mensagem na fila SQS, o evento recebido pela Lambda terá a seguinte estrutura:

```json
{
  "Records": [
    {
      "messageId": "12345678-1234-1234-1234-123456789012",
      "receiptHandle": "AQEBwJnKyrHigUMZj6rYigCgxlaS3SLy0a...",
      "body": "{\"Type\":\"Notification\",\"MessageId\":\"12345678-1234-1234-1234-123456789012\",\"TopicArn\":\"arn:aws:sns:us-east-1:123456789012:MyTopic\",\"Subject\":\"MySubject\",\"Message\":\"{\\\"teste\\\": \\\"banana\\\"}\",\"Timestamp\":\"2023-06-18T12:00:00.000Z\",\"SignatureVersion\":\"1\",\"Signature\":\"EXAMPLE\",\"SigningCertURL\":\"https://sns.us-east-1.amazonaws.com/SimpleNotificationService-12345678901234567890.pem\",\"UnsubscribeURL\":\"https://sns.us-east-1.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:us-east-1:123456789012:MyTopic:2bcfbf39-05c3-41de-beaa-fcfcc21c8f55\",\"MessageAttributes\":{}}",
      "attributes": {
        "ApproximateReceiveCount": "1",
        "SentTimestamp": "1632736816827",
        "SenderId": "AIDAIENQZJOLO23YVJ4VO",
        "ApproximateFirstReceiveTimestamp": "1632736816830"
      },
      "messageAttributes": {},
      "md5OfBody": "098f6bcd4621d373cade4e832627b4f6",
      "eventSource": "aws:sqs",
      "eventSourceARN": "arn:aws:sqs:us-east-1:123456789012:MyQueue",
      "awsRegion": "us-east-1"
    }
  ]
}
```

### Extraindo o corpo da mensagem na Lambda
Para mapear a mensagem recebida de um evento SQS para um objeto Java, você pode criar uma classe de mapeamento que represente a estrutura da mensagem e usar o `ObjectMapper` do Jackson para converter a mensagem JSON em uma instância dessa classe. 

#### 1. Definir a Classe do Objeto de Mensagem

Crie uma classe para representar a estrutura da mensagem SNS:

```java
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SNSMessage {
    private String teste;

    public SNSMessage() {}

    public SNSMessage(String teste) {
        this.teste = teste;
    }

    public String getTeste() {
        return teste;
    }

    public void setTeste(String teste) {
        this.teste = teste;
    }

    @Override
    public String toString() {
        return "SNSMessage{" +
                "teste='" + teste + '\'' +
                '}';
    }
}
```

#### 2. Atualizar o Manipulador Lambda

Atualize seu manipulador Lambda para usar o `ObjectMapper` do Jackson para converter a mensagem SNS em uma instância da classe `SNSMessage`:

```java
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SQSMessageHandler implements RequestHandler<SQSEvent, Void> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage record : event.getRecords()) {
            try {
                // Extrair o corpo da mensagem SQS
                JsonNode sqsBody = objectMapper.readTree(record.getBody());

                // Extrair a mensagem SNS
                String snsMessageString = sqsBody.get("Message").asText();
                SNSMessage snsMessage = objectMapper.readValue(snsMessageString, SNSMessage.class);

                // Agora você tem o objeto SNSMessage
                System.out.println(snsMessage); // Isso deve imprimir SNSMessage{teste='banana'}
                
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Erro ao processar a mensagem", e);
            }
        }
        return null;
    }
}
```

#### 3. Exemplo de Evento para Testes

Você pode usar o exemplo de evento fornecido anteriormente para testar o mapeamento:

```java
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;

public class LocalTest {

    public static void main(String[] args) {
        SQSMessageHandler handler = new SQSMessageHandler();
        ObjectMapper objectMapper = new ObjectMapper();

        String sqsMessageBody = "{\"Type\":\"Notification\",\"MessageId\":\"12345678-1234-1234-1234-123456789012\",\"TopicArn\":\"arn:aws:sns:us-east-1:123456789012:MyTopic\",\"Subject\":\"MySubject\",\"Message\":\"{\\\"teste\\\": \\\"banana\\\"}\",\"Timestamp\":\"2023-06-18T12:00:00.000Z\",\"SignatureVersion\":\"1\",\"Signature\":\"EXAMPLE\",\"SigningCertURL\":\"https://sns.us-east-1.amazonaws.com/SimpleNotificationService-12345678901234567890.pem\",\"UnsubscribeURL\":\"https://sns.us-east-1.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:us-east-1:123456789012:MyTopic:2bcfbf39-05c3-41de-beaa-fcfcc21c8f55\",\"MessageAttributes\":{}}";

        SQSEvent.SQSMessage sqsMessage = new SQSEvent.SQSMessage();
        sqsMessage.setBody(sqsMessageBody);

        SQSEvent sqsEvent = new SQSEvent();
        sqsEvent.setRecords(Collections.singletonList(sqsMessage));

        handler.handleRequest(sqsEvent, null);
    }
}
```
