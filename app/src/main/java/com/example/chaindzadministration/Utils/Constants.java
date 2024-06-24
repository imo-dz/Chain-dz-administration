package com.example.chaindzadministration.Utils;

public class Constants
{
    /* from activity to another */
    /*
        Intent intent = new Intent(MainActivity.this, ClientHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
     */
    /* snack bar */
    /*
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.sign_in_layout),
                 String.valueOf(getResources().getString(R.string.something_wrong)), Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(),R.color.error));
        snackbar.show();
     */
    public static final String ACCOUNT_TYPE_CLIENT = "Client";
    public static final String ACCOUNT_TYPE_ADMIN = "Admin";
    public static final String TYPE = "type";
    public static final String ACCOUNT_TYPE_STOCK_MANAGER = "Stock Manager";
    public static final String ACCOUNT_TYPE_CLAIM_MANAGER = "Claim Manager";
    public static final String ACCOUNT_TYPE_PRODUCTION_MANGER = "Production Manager";

    public static final String ACCOUNT_TYPE = "accountType";

    public static final String USERS_COLLECTION = "Users";


    public static final String[] DZ_WILAYAS = {
            "Adrar",
            "Chlef",
            "Laghouat",
            "Oum El Bouaghi",
            "Batna",
            "Béjaïa",
            "Biskra",
            "Béchar",
            "Blida",
            "Bouira",
            "Tamanrasset",
            "Tébessa",
            "Tlemcen",
            "Tiaret",
            "Tizi Ouzou",
            "Alger",
            "Djelfa",
            "Jijel",
            "Sétif",
            "Saïda",
            "Skikda",
            "Sidi Bel Abbès",
            "Annaba",
            "Guelma",
            "Constantine",
            "Médéa",
            "Mostaganem",
            "M'Sila",
            "Mascara",
            "Ouargla",
            "Oran",
            "El Bayadh",
            "Illizi",
            "Bordj Bou Arreridj",
            "Boumerdès",
            "El Tarf",
            "Tindouf",
            "Tissemsilt",
            "El Oued",
            "Khenchela",
            "Souk Ahras",
            "Tipaza",
            "Mila",
            "Aïn Defla",
            "Naâma",
            "Aïn Témouchent",
            "Ghardaïa",
            "Relizane"
    };


    public static final String PRODUCTS_COLLECTION = "Products" ;
    public static final String PRODUCTS_LOT = "Products Lot";
    public static final String CLAIM_ORDERS_COLLECTION = "Claim Orders";
}
