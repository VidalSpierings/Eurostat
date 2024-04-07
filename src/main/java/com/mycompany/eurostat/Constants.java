package com.mycompany.eurostat;

/**
 * App's Constants class (values that won't change and may be used troughout different parts of the application).
 * @author Vidal Spierings
 */

public final class Constants {

    
    public static final String API_LINK = "https://ec.europa.eu/eurostat/api/dissemination/statistics/1.0/data/isoc_cisci_sp20?format=JSON&lang=en&freq=A&ind_type=IND_TOTAL&indic_is=I_MPP1&indic_is=I_MPP1_SSA&indic_is=I_MPP1_SSM&indic_is=I_MPP1_SSX&indic_is=I_MPP1_SSZ&indic_is=I_MPP1_LST&indic_is=I_MPP1_RA&indic_is=I_MPP1_RAX&indic_is=I_MPP1_RAKX&indic_is=I_MPP1_RANA&unit=PC_IND&geo=BE&geo=BG&geo=CZ&geo=DK&geo=DE&geo=EE&geo=IE&geo=EL&geo=ES&geo=HR&geo=IT&geo=CY&geo=LV&geo=LT&geo=LU&geo=HU&geo=MT&geo=NL&geo=AT&geo=PL&geo=PT&geo=RO&geo=SI&geo=SK&geo=FI&geo=SE&geo=IS&geo=NO&geo=UK&geo=BA&geo=ME&geo=MK&geo=AL&geo=RS&geo=XK&time=2020";

    
    public static final String DATABASE_LINK = "jdbc:mysql://localhost:3306/eurostat?useSSL=false";

    
    public static final String DATABASE_USER_USERNAME = "admin_eurostat";

    
    public static final String DATABASE_USER_PASSWORD = "hyxha6-vuhzoP-jawjaw";
	
}
