package mx.itesm.rueschan.moviles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PreferencesAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
    }

    public void changeMain(View v) {
        Intent init = new Intent(this, MainActivity.class);
        startActivity(init);
    }

}
