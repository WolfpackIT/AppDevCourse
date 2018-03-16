package nl.wolfpack.emailwolfpack.slack;

import android.os.AsyncTask;

public class ShoutRunnable implements Runnable {
    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }
}


class ShoutTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... voids) {

        return null;
    }
}
