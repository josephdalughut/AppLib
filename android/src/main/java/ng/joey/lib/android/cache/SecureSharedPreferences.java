package ng.joey.lib.android.cache;

import android.content.Context;
import android.content.SharedPreferences;

import ng.joey.lib.java.security.Crypto;
import ng.joey.lib.java.util.Value;

import java.util.Map;
import java.util.Set;

/**
 * Created by Joey Dalughut on 8/10/16 at 5:46 PM,
 * Project: Litigy Libraries.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public abstract class SecureSharedPreferences implements SharedPreferences {

    protected SharedPreferences delegate;

    public SecureSharedPreferences(Context context){
        delegate = context.getSharedPreferences(getName(), Context.MODE_PRIVATE);
    }

    /**
     * The name of the SharedPreferences implementation.
     * @return the name to be used
     */
    public abstract String getName();

    /**
     * A password for an extra layer of security. Return null to use the default layer of security only
     * @return the password to be used
     */
    public abstract String getPassword();

    private String passwordize(String string){
        return Value.IS.emptyValue(string) ? string : getPassword()+string;
    }

    @Override
    public Set<String> getStringSet(String arg0, Set<String> arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, ?> getAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        final String value = delegate.getString(Crypto.HASH.SHA._224(passwordize(key)), null);
        try {
            return !Value.IS.emptyValue(value) ? Boolean.parseBoolean(Crypto.AES.decrypt(key, value)) : defValue;
        } catch (Exception e) {
            return defValue;
        }
    }

    @Override
    public float getFloat(String key, float defValue) {
        final String value = delegate.getString(Crypto.HASH.SHA._224(passwordize(key)), null);
        try {
            return !Value.IS.emptyValue(value) ? Float.parseFloat(Crypto.AES.decrypt(key, value)) : defValue;
        } catch (Exception e) {
            return defValue;
        }
    }

    @Override
    public int getInt(String key, int defValue) {
        final String value = delegate.getString(Crypto.HASH.SHA._224(passwordize(key)), null);
        try {
            return !Value.IS.emptyValue(value) ? Integer.parseInt(Crypto.AES.decrypt(key, value)) : defValue;
        } catch (Exception e) {
            return defValue;
        }
    }

    @Override
    public long getLong(String key, long defValue) {
        final String value = delegate.getString(Crypto.HASH.SHA._224(passwordize(key)), null);
        try {
            return !Value.IS.emptyValue(value) ? Long.parseLong(Crypto.AES.decrypt(key, value)) : defValue;
        } catch (Exception e) {
            return defValue;
        }
    }

    public Long getLong(String key, Long defValue){
        final String value = delegate.getString(Crypto.HASH.SHA._224(passwordize(key)), null);
        try {
            return !Value.IS.emptyValue(value) ? Long.parseLong(Crypto.AES.decrypt(key, value)) : defValue;
        }catch (Exception e){
            return defValue;
        }
    }

    @Override
    public String getString(String key, String defValue) {
        final String value = delegate.getString(Crypto.HASH.SHA._224(passwordize(key)), null);
        try {
            return !Value.IS.emptyValue(value) ? Crypto.AES.decrypt(key, value) : defValue;
        } catch (Exception e) {
            return defValue;
        }
    }

    public class Editor implements SharedPreferences.Editor {
        protected SharedPreferences.Editor delegate;

        public Editor() {
            this.delegate = SecureSharedPreferences.this.delegate.edit();
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            try {
                delegate.putString(Crypto.HASH.SHA._224(passwordize(key)), Crypto.AES.encrypt(key, Value.TO.stringValue(value)));
            } catch (Exception e) {

            }
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            try {
                delegate.putString(Crypto.HASH.SHA._224(passwordize(key)), Crypto.AES.encrypt(key, Value.TO.stringValue(value)));
            } catch (Exception e) {

            }
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            clear();
            try {
                delegate.putString(Crypto.HASH.SHA._224(passwordize(key)), Crypto.AES.encrypt(key, Value.TO.stringValue(value)));
            } catch (Exception e) {

            }
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            try {
                delegate.putString(Crypto.HASH.SHA._224(passwordize(key)), Crypto.AES.encrypt(key, Value.TO.stringValue(value)));
            } catch (Exception e) {

            }
            return this;
        }

        @Override
        public Editor putString(String key, String value) {
            try {
                delegate.putString(Crypto.HASH.SHA._224(passwordize(key)), Crypto.AES.encrypt(key, value));
            } catch (Exception e) {

            }
            return this;
        }

        @Override
        public void apply() {
            delegate.apply();
        }

        @Override
        public Editor clear() {
            delegate.clear();
            return this;
        }

        @Override
        public boolean commit() {
            return delegate.commit();
        }

        @Override
        public Editor remove(String s) {
            try {
                delegate.remove(Crypto.HASH.SHA._224(s));
            }catch(Exception e){

            }
            return this;
        }

        @Override
        public android.content.SharedPreferences.Editor putStringSet(
                String arg0, Set<String> arg1) {
            // TODO Auto-generated method stub
            return null;
        }
    }

    public Editor edit() {
        return new Editor();
    }



    @Override
    public boolean contains(String s) {
        return delegate.contains(Crypto.HASH.SHA._224(s));
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        delegate.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        delegate.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }


}