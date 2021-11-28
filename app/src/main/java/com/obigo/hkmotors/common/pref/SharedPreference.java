package com.obigo.hkmotors.common.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.Serializable;

/**
 * Preference to save
 */
public class SharedPreference implements Serializable {
    private SharedPreferences hsePrefer;
    private SharedPreferences.Editor editor;

    private static Context nContext;


    /**
     * Constructor
     */
    public SharedPreference() {}

    /**
     * Constructor with context
     * @param pContext
     */
    public SharedPreference(Context pContext) {
        SharedPreference.nContext = pContext;
        hsePrefer = PreferenceManager.getDefaultSharedPreferences(nContext);
        editor = hsePrefer.edit();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // SET METHOD
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setDPWD(String pwd) {                   editor.putString("DPWD", pwd);	                    editor.commit();	}
    public void setPWD(String pwd) {                    editor.putString("PWD", pwd);	                    editor.commit();	}
    public void setFD(boolean fd) {                     editor.putBoolean("FD", fd);	                    editor.commit();	}

    // set first default
    public void setFDEcoLevel(float ecoLevel) {         editor.putFloat("FDEcoLevel", ecoLevel);	        editor.commit();    }
    public void setFDMaxPower(float maxPower) {         editor.putFloat("FDMaxPower", maxPower);            editor.commit();    }
    public void setFDAcceleration(float acceleration) {	editor.putFloat("FDAcceleration", acceleration);    editor.commit();	}
    public void setFDDeceleration(float deceleration) {	editor.putFloat("FDDeceleration", deceleration);    editor.commit();	}
    public void setFDResponse(float response) {		    editor.putFloat("FDResponse", response);	        editor.commit();	}

    // set second defualt
    public void setSDEcoLevel(float ecoLevel) {		    editor.putFloat("SDEcoLevel", ecoLevel);	        editor.commit();	}
    public void setSDMaxPower(float maxPower) {         editor.putFloat("SDMaxPower", maxPower);            editor.commit();    }
    public void setSDAcceleration(float acceleration) {	editor.putFloat("SDAcceleration", acceleration);	editor.commit();	}
    public void setSDDeceleration(float deceleration) {	editor.putFloat("SDDeceleration", deceleration);	editor.commit();	}
    public void setSDResponse(float response) {		    editor.putFloat("SDResponse", response);	        editor.commit();	}

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // GET METHOD
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getDPWD() {                           return hsePrefer.getString("DPWD", null);	                            }
    public String getPWD() {                            return hsePrefer.getString("PWD", null);	                            }
    public boolean getFD() {                            return hsePrefer.getBoolean("FD", false);	                            }

    // get first default
    public float getFDEcoLevel() {                      return hsePrefer.getFloat("FDEcoLevel", 0);	                            }
    public float getFDMaxPower() {                      return hsePrefer.getFloat("FDMaxPower", 0);	                            }
    public float getFDAcceleration() {                  return hsePrefer.getFloat("FDAcceleration", 0);	                        }
    public float getFDDeceleration() {                  return hsePrefer.getFloat("FDDeceleration", 0);	                        }
    public float getFDResponse() {                      return hsePrefer.getFloat("FDResponse", 0);	                            }

    // get secnod default
    public float getSDEcoLevel() {                      return hsePrefer.getFloat("SDEcoLevel", 0);	                            }
    public float getSDMaxPower() {                      return hsePrefer.getFloat("SDMaxPower", 0);	                            }
    public float getSDAcceleration() {                  return hsePrefer.getFloat("SDAcceleration", 0);	                        }
    public float getSDDeceleration() {                  return hsePrefer.getFloat("SDDeceleration", 0);	                        }
    public float getSDResponse() {                      return hsePrefer.getFloat("SDResponse", 0);	                            }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // REMOVE METHOD
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public void removeAllPreferences() {
        editor.clear();
        editor.commit();
    }
}