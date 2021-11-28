package com.obigo.hkmotors.common.obd;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.obigo.hkmotors.view.LoginActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


/**
 * This service is primarily responsible for establishing and maintaining a
 * permanent connection between the device where the application runs and a more
 * OBD Bluetooth interface.
 *
 * Secondarily, it will serve as a repository of ObdCommandJobs and at the same
 * time the application state-machine.
 */
public class MockObdGatewayService extends AbstractGatewayService {

    private static final String TAG = MockObdGatewayService.class.getName();

    private static Handler callBackMsg;
    public void startService(String remoteDevice, Handler resultHandler, BluetoothSocket mSoket) {
        Log.d(TAG, "Starting " + this.getClass().getName() + " service..");
        callBackMsg = resultHandler;

        // Let's configure the connection.
        Log.d(TAG, "Queing jobs for connection configuration..");
//        queueJob(new ObdCommandJob(new ObdResetCommand()));
////        queueJob(new ObdCommandJob(new EchoOffCommand()));
//
//    /*
//     * Will send second-time based on tests.
//     *
//     * TODO this can be done w/o having to queue jobs by just issuing
//     * command.run(), command.getResult() and validate the result.
//     */
//        queueJob(new ObdCommandJob(new EchoOffCommand()));
//        queueJob(new ObdCommandJob(new LineFeedOffCommand()));
//        queueJob(new ObdCommandJob(new TimeoutCommand(62)));
//
//        // For now set protocol to AUTO
//        queueJob(new ObdCommandJob(new SelectProtocolCommand(ObdProtocols.AUTO)));
//        queueJob(new ObdCommandJob(new SelectProtocolCommand(ObdProtocols.valueOf("ISO_15765_4_CAN_B"))));

        queueCounter = 0L;
        Log.d(TAG, "Initialization jobs queued.");

        isRunning = true;
    }


    /**
     * Runs the queue until the service is stopped
     */
    protected void executeQueue() {
//        Log.d(TAG, "Executing queue..");
        while (!Thread.currentThread().isInterrupted()) {
            ObdCommandJob job = null;
            try {
                job = jobsQueue.take();

//                Log.d(TAG, "Taking job[" + job.getId() + "] from queue..");

                if (job.getState().equals(ObdCommandJob.ObdCommandJobState.NEW)) {
//                    Log.d(TAG, "Job state is NEW. Run it..");
                    job.setState(ObdCommandJob.ObdCommandJobState.RUNNING);
                    Log.d(TAG, "근데 이건 언제타는거냐;;;;; " + job.getCommand().getName());
                    job.getCommand().run(new ByteArrayInputStream("41 00 00 00>41 00 00 00>41 00 00 00>".getBytes()), new ByteArrayOutputStream());
                } else {
                    Log.e(TAG, "Job state was not new, so it shouldn't be in queue. BUG ALERT!");
                }
            } catch (InterruptedException i) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
                if (job != null) {
                    job.setState(ObdCommandJob.ObdCommandJobState.EXECUTION_ERROR);
                }
                Log.e(TAG, "Failed to run command. -> " + e.getMessage());
            }

            if (job != null) {
                Log.d(TAG, "Job is finished.");
                job.setState(ObdCommandJob.ObdCommandJobState.FINISHED);

                final ObdCommandJob job2 = job;
                ((LoginActivity) ctx).runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
//                                ((LoginActivity) ctx).setUIFromOBD(job2);
                                Message msg = Message.obtain(callBackMsg, 999, job2);
                                callBackMsg.sendMessage(msg);
                            }
                        }
                );
            }
        }
    }


    /**
     * Stop OBD connection and queue processing.
     */
    public void stopService() {
        Log.d(TAG, "Stopping service..");

        notificationManager.cancel(NOTIFICATION_ID);
        jobsQueue.clear();
        isRunning = false;

        // kill service
        stopSelf();
    }

}
