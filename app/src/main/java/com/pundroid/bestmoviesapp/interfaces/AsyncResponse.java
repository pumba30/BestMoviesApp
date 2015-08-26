package com.pundroid.bestmoviesapp.interfaces;

import org.json.JSONException;

/**
 * Created by pumba30 on 25.08.2015.
 * Интерфейс для возврата результата из AsyncTask
 */
public interface AsyncResponse {
    void processFinish(String result) throws JSONException;
}
