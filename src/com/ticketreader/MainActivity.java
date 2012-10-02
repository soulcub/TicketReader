package com.ticketreader;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.ticketreader.mathcore.Range;
import com.ticketreader.mathcore.RangeManager;
import com.ticketreader.surface.camera.MakePhotoActivity;

public class MainActivity extends Activity {

	private static final int REQUEST_CODE = 1;

	public final static String EXTRA_MESSAGE = "com.ticketreader.MainActivity.EditTextMessage";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//loading range's formulas
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void pickImage(View View) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
		// try {
		// // We need to recyle unused bitmaps
		// if (bitmap != null) {
		// bitmap.recycle();
		// }
		// InputStream stream =
		// getContentResolver().openInputStream(data.getData());
		// bitmap = BitmapFactory.decodeStream(stream);
		// stream.close();
		// imageView.setImageBitmap(bitmap);
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		super.onActivityResult(requestCode, resultCode, data);
	}

	/** Called when the user selects the Run button */
	public void sendMessage(View view) {
//		Intent intent = new Intent(this, DisplayMessageActivity.class);
//		EditText editText = (EditText) findViewById(R.id.EditFieldId);
//		String message = editText.getText().toString();
//		intent.putExtra(EXTRA_MESSAGE, message);
//		startActivity(intent);
		RangeManager rm = new RangeManager();
		ArrayList<Range> rangeList = rm.getRangeList();
		
		EditText editField = (EditText)findViewById(R.id.EditFieldId);
		String str = editField.getText().toString();
		for(Range range: rangeList){
			int rez = range.getRange().indexOf(str);
			if(rez < 0){
				editField.setText("Fuck");
			}
			else{
				editField.setText("Good");
			}
		}
	}

	/** Called when the user selects the Test button */
	public void testCamera(View view) {
		Intent intent = new Intent(this, MakePhotoActivity.class);
		startActivity(intent);
	}
}
