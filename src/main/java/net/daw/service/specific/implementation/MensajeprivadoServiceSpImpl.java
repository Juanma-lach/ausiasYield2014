/*
 * Copyright (C) July 2014 Rafael Aznar
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.daw.service.specific.implementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.daw.bean.generic.specific.implementation.MensajeprivadoBeanGenSpImpl;
import net.daw.dao.specific.implementation.MensajeprivadoDaoSpcImpl;
import net.daw.helper.AppConfigurationHelper;
import net.daw.helper.EncodingUtilHelper;
import net.daw.helper.ExceptionBooster;
import net.daw.helper.FilterBeanHelper;
import net.daw.service.publicinterface.MetaServiceInterface;
import net.daw.service.publicinterface.TableServiceInterface;
import net.daw.service.publicinterface.ViewServiceInterface;

public class MensajeprivadoServiceSpImpl implements TableServiceInterface, ViewServiceInterface, MetaServiceInterface {

    protected Connection oConnection = null;
    protected String strObjectName = null;
    protected String strPojo = null;

    public MensajeprivadoServiceSpImpl(String strObject, String pojo, Connection con) {
        strObjectName = strObject;
        oConnection = con;
        strPojo = Character.toUpperCase(pojo.charAt(0)) + pojo.substring(1);
    }

    @Override
    public void setSource(String source) throws Exception {
        strObjectName = source;
    }

    @Override
    public void setPojo(String pojo) throws Exception {
        strPojo = Character.toUpperCase(pojo.charAt(0)) + pojo.substring(1);
    }

    @Override
    public String remove(Integer id) throws Exception {
        String resultado = null;
        try {
            oConnection.setAutoCommit(false);
            MensajeprivadoDaoSpcImpl oMensajeprivadoDAO = new MensajeprivadoDaoSpcImpl(strObjectName, oConnection);
            MensajeprivadoBeanGenSpImpl oMensajeprivado = new MensajeprivadoBeanGenSpImpl(id);
            Map<String, String> data = new HashMap<>();
            oMensajeprivadoDAO.remove(oMensajeprivado);
            data.put("status", "200");
            data.put("message", "se ha eliminado el registro con id=" + oMensajeprivado.getId());
            Gson gson = new Gson();
            resultado = gson.toJson(data);
            oConnection.commit();
        } catch (Exception ex) {
            oConnection.rollback();
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":remove ERROR: " + ex.getMessage()));
        }
        return resultado;
    }

    @Override
    public String set(String jason) throws Exception {
        String resultado = null;
        try {
            oConnection.setAutoCommit(false);
            MensajeprivadoDaoSpcImpl oMensajeprivadoDAO = new MensajeprivadoDaoSpcImpl(strObjectName, oConnection);
            MensajeprivadoBeanGenSpImpl oMensajeprivado = new MensajeprivadoBeanGenSpImpl();
            Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
            jason = EncodingUtilHelper.decodeURIComponent(jason);
            oMensajeprivado = gson.fromJson(jason, oMensajeprivado.getClass());
            oMensajeprivado = oMensajeprivadoDAO.set(oMensajeprivado);
            Map<String, String> data = new HashMap<>();
            data.put("status", "200");
            data.put("message", Integer.toString(oMensajeprivado.getId()));
            resultado = gson.toJson(data);
            oConnection.commit();
        } catch (Exception ex) {
            oConnection.rollback();
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":set ERROR: " + ex.getMessage()));
        }
        return resultado;
    }

    public String set2(String jason, int idusuario, int idtipousuario) throws Exception {
        String resultado = null;
        try {
            oConnection.setAutoCommit(false);
            MensajeprivadoDaoSpcImpl oMensajeprivadoDAO = new MensajeprivadoDaoSpcImpl(strObjectName, oConnection);
            MensajeprivadoBeanGenSpImpl oMensajeprivado = new MensajeprivadoBeanGenSpImpl();
            Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
            jason = EncodingUtilHelper.decodeURIComponent(jason);
            oMensajeprivado = gson.fromJson(jason, oMensajeprivado.getClass());

            if (idusuario != oMensajeprivado.getId_usuario_1() && idtipousuario != 1) {
                Map<String, String> data = new HashMap<>();
                data.put("message", "Su ID no corresponde al usuario que envía");
                resultado = gson.toJson(data);
            } else if (oMensajeprivado.getId_usuario_1() == oMensajeprivado.getId_usuario_2()) {
                Map<String, String> data = new HashMap<>();
                data.put("message", "No puedes mandarte un mensaje privado a tí mismo");
                resultado = gson.toJson(data);
            } else {
                oMensajeprivado = oMensajeprivadoDAO.set(oMensajeprivado);
                Map<String, String> data = new HashMap<>();
                data.put("status", "200");
                data.put("message", Integer.toString(oMensajeprivado.getId()));
                resultado = gson.toJson(data);
            }
            oConnection.commit();
        } catch (Exception ex) {
            oConnection.rollback();
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":set ERROR: " + ex.getMessage()));
        }
        return resultado;
    }

    @Override
    public String get(Integer id) throws Exception {
        String data = null;
        try {
            oConnection.setAutoCommit(false);
            MensajeprivadoDaoSpcImpl oMensajeprivadoDAO = new MensajeprivadoDaoSpcImpl(strObjectName, oConnection);
            MensajeprivadoBeanGenSpImpl oMensajeprivado = new MensajeprivadoBeanGenSpImpl(id);
            oMensajeprivado = oMensajeprivadoDAO.get(oMensajeprivado, AppConfigurationHelper.getJsonDepth());
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("dd/MM/yyyy HH:mm:ss");
            Gson gson = gsonBuilder.create();
            data = gson.toJson(oMensajeprivado);
            oConnection.commit();
        } catch (Exception ex) {
            oConnection.rollback();
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":get ERROR: " + ex.getMessage()));
        }
        return data;
    }

    @Override
    public String getPage(int intRegsPerPag, int intPage, ArrayList<FilterBeanHelper> alFilter, HashMap<String, String> hmOrder) throws Exception {
        String data = null;
        try {
            oConnection.setAutoCommit(false);
            MensajeprivadoDaoSpcImpl oMensajeprivadoDAO = new MensajeprivadoDaoSpcImpl(strObjectName, oConnection);
            List<MensajeprivadoBeanGenSpImpl> oMensajeprivados = oMensajeprivadoDAO.getPage(intRegsPerPag, intPage, alFilter, hmOrder);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("dd/MM/yyyy HH:mm:ss");
            Gson gson = gsonBuilder.create();
            data = gson.toJson(oMensajeprivados);
            data = "{\"list\":" + data + "}";
            oConnection.commit();
        } catch (Exception ex) {
            oConnection.rollback();
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getPage ERROR: " + ex.getMessage()));
        }
        return data;
    }

    @Override
    public String getPages(int intRegsPerPag, ArrayList<FilterBeanHelper> alFilter) throws Exception {
        String data = null;
        try {
            oConnection.setAutoCommit(false);
            MensajeprivadoDaoSpcImpl oMensajeprivadoDAO = new MensajeprivadoDaoSpcImpl(strObjectName, oConnection);
            int pages = oMensajeprivadoDAO.getPages(intRegsPerPag, alFilter);
            data = "{\"data\":\"" + Integer.toString(pages) + "\"}";
            oConnection.commit();
        } catch (Exception ex) {
            oConnection.rollback();
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getPages ERROR: " + ex.getMessage()));
        }
        return data;
    }

    @Override
    public String getCount(ArrayList<FilterBeanHelper> alFilter) throws Exception {
        String data = null;
        try {
            oConnection.setAutoCommit(false);
            MensajeprivadoDaoSpcImpl oMensajeprivadoDAO = new MensajeprivadoDaoSpcImpl(strObjectName, oConnection);
            int registers = oMensajeprivadoDAO.getCount(alFilter);
            data = "{\"data\":\"" + Integer.toString(registers) + "\"}";
            oConnection.commit();

        } catch (Exception ex) {
            oConnection.rollback();
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getCount ERROR: " + ex.getMessage()));
        }
        return data;
    }

    @Override
    public String getPrettyColumns() throws Exception {
        String data = null;
        ArrayList<String> alColumns = null;
        try {
            oConnection.setAutoCommit(false);
            MensajeprivadoDaoSpcImpl oMensajeprivadoDAO = new MensajeprivadoDaoSpcImpl(strObjectName, oConnection);
            alColumns = oMensajeprivadoDAO.getPrettyColumnsNames();
            data = new Gson().toJson(alColumns);
            //data = "{\"data\":" + data + "}";
            oConnection.commit();
        } catch (Exception ex) {
            oConnection.rollback();
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getPrettyColumns ERROR: " + ex.getMessage()));
        }
        return data;
    }

    @Override
    public String getColumns() throws Exception {
        String data = null;
        try {
            oConnection.setAutoCommit(false);
            ArrayList<String> alColumns = null;
            MensajeprivadoDaoSpcImpl oMensajeprivadoDAO = new MensajeprivadoDaoSpcImpl(strObjectName, oConnection);
            alColumns = oMensajeprivadoDAO.getColumnsNames();
            data = new Gson().toJson(alColumns);
            //data = "{\"data\":" + data + "}";
            oConnection.commit();

        } catch (Exception ex) {
            oConnection.rollback();
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getColumns ERROR: " + ex.getMessage()));
        }
        return data;
    }

    @Override
    public String getAggregateViewOne(Integer id) throws Exception {
        String data = null;
        try {
            oConnection.setAutoCommit(false);
            String columns = this.getColumns();
            String prettyColumns = this.getPrettyColumns();
            //String types = this.getTypes();
            String one = this.get(id);
            data = "{\"data\":{"
                    + "\"columns\":" + columns
                    + ",\"prettyColumns\":" + prettyColumns
                    // + ",\"types\":" + types
                    + ",\"data\":" + one
                    + "}}";
            oConnection.commit();

        } catch (Exception ex) {
            oConnection.rollback();
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getAggregateViewOne ERROR: " + ex.getMessage()));
        }
        return data;
    }

    @Override
    public String getAggregateViewSome(int intRegsPerPag, int intPage, ArrayList<FilterBeanHelper> alFilter, HashMap<String, String> hmOrder) throws Exception {
        String data = null;
        try {
            oConnection.setAutoCommit(false);
            String columns = this.getColumns();
            String prettyColumns = this.getPrettyColumns();
            //String types = this.getTypes();
            String page = this.getPage(intRegsPerPag, intPage, alFilter, hmOrder);
            String pages = this.getPages(intRegsPerPag, alFilter);
            String registers = this.getCount(alFilter);
            data = "{\"data\":{"
                    + "\"columns\":" + columns
                    + ",\"prettyColumns\":" + prettyColumns
                    // + ",\"types\":" + types
                    + ",\"page\":" + page
                    + ",\"pages\":" + pages
                    + ",\"registers\":" + registers
                    + "}}";
            oConnection.commit();

        } catch (Exception ex) {
            oConnection.rollback();
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getAggregateViewSome ERROR: " + ex.getMessage()));
        }
        return data;
    }

    public String getAggregateViewSomeId(int intRegsPerPag, int intPage, ArrayList<FilterBeanHelper> alFilter, HashMap<String, String> hmOrder, int idusuario, int tipousuario) throws Exception {
        String data = null;
        try {
            oConnection.setAutoCommit(false);
            String columns = this.getColumns();
            String prettyColumns = this.getPrettyColumns();
            //String types = this.getTypes();
            String page = this.getPage(intRegsPerPag, intPage, alFilter, hmOrder);
            String pages = this.getPages(intRegsPerPag, alFilter);
            String registers = this.getCount(alFilter);
            data = "{\"data\":{"
                    + "\"columns\":" + columns
                    + ",\"prettyColumns\":" + prettyColumns
                    // + ",\"types\":" + types
                    + ",\"page\":" + page
                    + ",\"pages\":" + pages
                    + ",\"registers\":" + registers
                    + "}}";
            oConnection.commit();

        } catch (Exception ex) {
            oConnection.rollback();
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getAggregateViewSome ERROR: " + ex.getMessage()));
        }
        return data;
    }

    @Override
    public String updateOne(int intId, String strTabla, String strCampo, String strValor) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
