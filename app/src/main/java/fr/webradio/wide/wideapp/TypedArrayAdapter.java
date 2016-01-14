package fr.webradio.wide.wideapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dubois on 11/4/15.
 */

class TypedArrayAdapter extends BaseAdapter {
    private Context context;
    private final int[] drawableId; //tableau des identifiants des images à afficher
    private int imageView; //identifiant de l'imageView à afficher
    private int layout; // identifiant du layout des éléments
    private final String[] list,list2;
    private final Typeface myTypeface;

    /***
     * Récupère le tableau d'identifiants à afficher.
     * @param context l'activité utilisant cet adapter
     * @param resourceId l'identifiant de type R.array. désignant le tableau de drawables.
     * @param layout l'identifiant de la mise en page des éléments (R.layout....)
     * @param imageView l'identifiant de l'image dans le layout (R.id. ...)
     */
    public TypedArrayAdapter(Context context, int resourceId, int layout, int imageView) {

        //Les informations nécessaire au layout des éléments sont fournies en paramètre
        // du constructeur et sauvées dans des champs
        this.context = context;
        this.imageView = imageView;
        this.layout = layout;

        // On obtient le tableau d'ids des drawables à partir de la ressource, et on sauvegarde dans
        //un int[]

        TypedArray ids =  context.getResources().obtainTypedArray(resourceId);
        drawableId = new int[ids.length()];
        for(int i = 0; i < ids.length(); ++i){
            drawableId[i] = ids.getResourceId(i,0);
        }
        ids.recycle();

        list = context.getResources().getStringArray(R.array.liste);
        list2 = context.getResources().getStringArray(R.array.liste2);
        myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/raleway.ttf");
    }

    @Override
    public int getCount() {
        return drawableId.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Si convertView n'est pas déjà instancié, on le créé à partir du modèle
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(layout, parent, false);

        // On obtient la vue à configurer
        ImageView imageView = (ImageView) convertView.findViewById(this.imageView);

        // On choisit l'image à afficher
        imageView.setImageResource(drawableId[position]);



        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        TextView textView2 = (TextView) convertView.findViewById(R.id.textView2);
        textView2.setText(list2[position]);
        // On choisit l'image à afficher
        textView.setText(list[position]);

        textView.setTypeface(myTypeface); // Change la police du composant

        return convertView;
    }
}