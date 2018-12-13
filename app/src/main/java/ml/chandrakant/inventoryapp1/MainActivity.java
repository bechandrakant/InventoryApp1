package ml.chandrakant.inventoryapp1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ml.chandrakant.inventoryapp1.data.ProductContract.ProductEntry;
import ml.chandrakant.inventoryapp1.data.ProductDbHelper;

public class MainActivity extends AppCompatActivity {

    private EditText productName;
    private EditText productPrice;
    private EditText productQuantity;
    private EditText supplierName;
    private EditText supplierPhone;

    private TextView queryText;

    private Button insert;
    private Button query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productName = findViewById(R.id.product_name_et);
        productPrice = findViewById(R.id.product_price_et);
        productQuantity = findViewById(R.id.product_quantity_et);
        supplierName = findViewById(R.id.supplier_name_et);
        supplierPhone = findViewById(R.id.supplier_phone_et);

        insert = findViewById(R.id.insert_btn);
        query = findViewById(R.id.query_btn);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryData();
            }
        });
    }

    private void insertData() {
        ProductDbHelper dbHelper = new ProductDbHelper(this);
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        
        ContentValues products = new ContentValues();
        
        String storeProductName = productName.getText().toString();
        String storeProductPrice = productPrice.getText().toString();
        String storeProductQuantity = productQuantity.getText().toString();
        String storeSupplierName = supplierName.getText().toString();
        String storeSupplierPhone = supplierPhone.getText().toString();

        // Check for non empty data fields
        if (storeProductName.trim().equals("")) {
            Toast.makeText(MainActivity.this, "Please Enter Product Name!", Toast.LENGTH_SHORT).show();
        } else {
            products.put(ProductEntry.COLUMN_PRODUCT_NAME, storeProductName);
        }

        if (storeProductPrice.trim().equals("")) {
            Toast.makeText(MainActivity.this, "Please Enter Product Price!", Toast.LENGTH_SHORT).show();
        } else {
            products.put(ProductEntry.COLUMN_PRODUCT_PRICE, storeProductPrice);
        }

        if (storeProductQuantity.trim().equals("")) {
            Toast.makeText(MainActivity.this, "Please Enter Product Quantity!", Toast.LENGTH_SHORT).show();
        } else {
            products.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, storeProductQuantity);

        }
        if (storeSupplierName.trim().equals("")) {
            Toast.makeText(MainActivity.this, "Please Enter Supplier Name!", Toast.LENGTH_SHORT).show();
        } else {
            products.put(ProductEntry.COLUMN_SUPPLIER_NAME, storeSupplierName);
        }

        if (storeSupplierPhone.trim().equals("")) {
            Toast.makeText(MainActivity.this, "Please Enter Supplier Phone", Toast.LENGTH_SHORT).show();
        } else {
            products.put(ProductEntry.COLUMN_SUPPLIER_PHONE, storeSupplierPhone);
        }

        long rowInsertionID = sql.insert(ProductEntry.TABLE_NAME, null, products);
        if (rowInsertionID != -1) {
            Toast.makeText(this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
            productName.setText("");
            productPrice.setText("");
            productQuantity.setText("");
            supplierName.setText("");
            supplierPhone.setText("");
        } else {
            Toast.makeText(this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
        }

    }

    private void queryData() {
        ProductDbHelper dbHelper = new ProductDbHelper(this);
        SQLiteDatabase sql = dbHelper.getReadableDatabase();

        String[] columns = {ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_QUANTITY};

        Cursor cursor = sql.query(ProductEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                ProductEntry.COLUMN_PRODUCT_QUANTITY
        );

        queryText = findViewById(R.id.query_text);

        try {

            int productNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

            while (cursor.moveToNext()) {
                String productNameText = cursor.getString(productNameColumnIndex);
                int quantity = cursor.getInt(quantityColumnIndex);

                queryText.append("\n" + productNameText + " " + quantity + "\n"
                                    + "----------------------------------");
                queryText.setVisibility(View.VISIBLE);
            }
        } finally {
            cursor.close();
        }

    }
}
