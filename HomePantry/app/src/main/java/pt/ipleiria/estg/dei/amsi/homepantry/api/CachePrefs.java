package pt.ipleiria.estg.dei.amsi.homepantry.api;

import android.content.Context;
import android.content.SharedPreferences;

public class CachePrefs {

    private static final String PREF_NAME = "homepantry_cache_prefs";

    private static final String KEY_LAST_STOCK_SYNC = "last_stock_sync";
    private static final String KEY_LAST_PRODUTOS_SYNC = "last_produtos_sync";
    private static final String KEY_LAST_LISTAS_SYNC = "last_listas_sync";

    private final SharedPreferences prefs;

    public CachePrefs(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // ---------------- STOCK ----------------
    public void setLastStockSync(long timeMillis) {
        prefs.edit().putLong(KEY_LAST_STOCK_SYNC, timeMillis).apply();
    }

    public long getLastStockSync() {
        return prefs.getLong(KEY_LAST_STOCK_SYNC, 0);
    }

    // ---------------- PRODUTOS ----------------
    public void setLastProdutosSync(long timeMillis) {
        prefs.edit().putLong(KEY_LAST_PRODUTOS_SYNC, timeMillis).apply();
    }

    public long getLastProdutosSync() {
        return prefs.getLong(KEY_LAST_PRODUTOS_SYNC, 0);
    }

    // ---------------- LISTAS ----------------
    public void setLastListasSync(long timeMillis) {
        prefs.edit().putLong(KEY_LAST_LISTAS_SYNC, timeMillis).apply();
    }

    public long getLastListasSync() {
        return prefs.getLong(KEY_LAST_LISTAS_SYNC, 0);
    }

    // ---------------- LISTA PRODUTOS (DETAIL) ----------------
    public void setLastListaProdutosSync(int listaId, long timeMillis) {
        prefs.edit().putLong("last_lista_" + listaId + "_sync", timeMillis).apply();
    }

    public long getLastListaProdutosSync(int listaId) {
        return prefs.getLong("last_lista_" + listaId + "_sync", 0);
    public void setSelectedLocalId(int localId) {
    }
}
