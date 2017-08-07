package pl.kfrak.sdaselfie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int CAMERA_RESULT_REQUEST_CODE = 17000;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.image_view);
        Button button = (Button) findViewById(R.id.button);

        //bez clean code-u:
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent niebezpośredni
                //dlaczego odpalenie aparatu/kamery jest niesensowne? bo jak wrócić z p3wortoem do aplikacji? nie wrócimy.
                //A chcemy wrócić razem ze zrobionym zdjęciem. A activity komunikują się ze osbą intentami. Więc
                // chcemy zrobić intent ze zdjęciem który jest przekazany z pwrotem do naszej apki. StartActivityForResult
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //jesli intent nie znajdzie apki do zrobienia zdjęć to się wywali, więc zabezpieczenie:
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, CAMERA_RESULT_REQUEST_CODE);
                    //czym jest 1?
                } else {
                    Log.e(TAG, "No activity found");
                }
            }
        });
    }

    @Override
    //request code = ta "17000" w onClicku, RQ określa jednoznacznie z czego otrzymujemy wynik
    //odpali sie zawsze gdy jakakolwiek apka zwróci nam wynik, więc if, aby porównać vczy to zwrotka z naszej kamery
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_RESULT_REQUEST_CODE && resultCode == RESULT_OK) {
            //wysłanie danych w intentach - za pomocą boundle-a
            //boundle to taka paczka: klucz+wartość; klucz+warytość
            Bundle bundle = data.getExtras();
            //z dokumentacji:
            Object returnedObject = bundle.get("data");
            if (returnedObject instanceof Bitmap) {
                Bitmap bitmap = (Bitmap) returnedObject;
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
