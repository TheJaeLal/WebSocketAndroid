package lal.jay.websocketdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.ws.WebSocketProtocol;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private TextView outputView;

    private OkHttpClient client;

    private static final int NORMAL_CLOSURE_STATUS = 1000;

    private final class EchoWebSocketListener extends WebSocketListener{

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
//            super.onOpen(webSocket, response);
            webSocket.send("Hello World!");
            webSocket.close(NORMAL_CLOSURE_STATUS,"Good bye!");
        }

        //two types of messages, string and Bytes..
//        @Override
//        public void onMessage(WebSocket webSocket, ByteString bytes) {
//            //super.onMessage(webSocket, bytes);
//        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            //super.onMessage(webSocket, text);
            output("Server:"+text);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        outputView = (TextView)findViewById(R.id.tv_output);

        client = new OkHttpClient();
    }


    //onClickListener for start button
    public void onStart(View view) {
        Request request = new Request.Builder().url("ws://echo.websocket.org").build();

        EchoWebSocketListener listener = new EchoWebSocketListener();

        WebSocket ws = client.newWebSocket(request,listener);

        ws.send("This is the first text officially sent!");

        client.dispatcher().executorService().shutdown();

    }

    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                outputView.setText(outputView.getText().toString() + "\n\n" + txt);
            }
        });
    }
}
