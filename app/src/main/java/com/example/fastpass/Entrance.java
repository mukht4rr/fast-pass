package com.example.fastpass;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fastpass.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Locale;

public class Entrance extends AppCompatActivity {

    private EditText edittext2;
    private EditText edittext3;
    private TextView price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        Spinner spinner = findViewById(R.id.spinner);

        // Define dropdown options
        String[] locations = new String[]{
                "Kariakoo", "Zanzibar Park", "Garden", "Kisakasaka", "Forodhani"
        };

        // Create adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, locations
        );

        // Set dropdown layout style
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapter to spinner
        spinner.setAdapter(adapter);

        edittext2 = findViewById(R.id.edittext2);
        edittext3 = findViewById(R.id.edittext3);
        price = findViewById(R.id.price);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing before text changes
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Calculate the total price whenever the text changes
                calculateTotalPrice();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing after text changes
            }
        };

        edittext2.addTextChangedListener(textWatcher);
        edittext3.addTextChangedListener(textWatcher);

        Button paymentButton = findViewById(R.id.payment);
        paymentButton.setOnClickListener(view -> generateQRCode());
    }

    private int calculateTotalPrice() {
        int adultPrice = 2000;
        int childPrice = 1000;

        int numAdults = getNumberFromEditText(edittext2);
        int numChildren = getNumberFromEditText(edittext3);

        // Calculate the total price
        int totalPrice = (numAdults * adultPrice) + (numChildren * childPrice);

        // Display the total price in the TextView
        price.setText(String.format(Locale.getDefault(), "%,d Tsh", totalPrice));
        return totalPrice;
    }

    private int getNumberFromEditText(EditText editText) {
        String text = editText.getText().toString().trim();
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

    private void generateQRCode() {
        String spinnerSelection = ((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString();
        int numAdults = getNumberFromEditText(edittext2);
        int numChildren = getNumberFromEditText(edittext3);
        int totalPrice = calculateTotalPrice();

        // Construct the message to be encoded in the QR code
        String qrMessage = String.format(Locale.getDefault(), "Location: %s\nAdults: %d\nChildren: %d\nTotal Price: %,d Tsh",
                spinnerSelection, numAdults, numChildren, totalPrice);

        // Generate QR code bitmap
        try {
            Bitmap qrCodeBitmap = generateQRCodeBitmap(qrMessage);
            showQRCodeDialog(qrCodeBitmap);
        } catch (WriterException e) {
            Toast.makeText(this, "Failed to generate QR code", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private Bitmap generateQRCodeBitmap(String qrMessage) throws WriterException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(qrMessage, BarcodeFormat.QR_CODE, 400, 400);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }

    private void showQRCodeDialog(Bitmap qrCodeBitmap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("QR Code Payment Details");
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(qrCodeBitmap);
        builder.setView(imageView);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
