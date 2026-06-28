package com.optiontype;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optiontype.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class OptionTypeTest {

    private final ObjectMapper mapper = new ObjectMapper();

    // --- Construction ---

    @Test
    void testEuropeanConstruction() {
        OptionType opt = new OptionType.European();
        assertInstanceOf(OptionType.European.class, opt);
    }

    @Test
    void testAmericanConstruction() {
        OptionType opt = new OptionType.American();
        assertInstanceOf(OptionType.American.class, opt);
    }

    @Test
    void testBermudaConstruction() {
        OptionType opt = new OptionType.Bermuda(List.of(30.0, 60.0, 90.0));
        assertInstanceOf(OptionType.Bermuda.class, opt);
    }

    @Test
    void testAsianConstruction() {
        OptionType opt = new OptionType.Asian(AsianAveragingType.ARITHMETIC);
        assertInstanceOf(OptionType.Asian.class, opt);
    }

    @Test
    void testBarrierConstruction() {
        OptionType opt = new OptionType.Barrier(BarrierType.UP_AND_IN, 120.0, 5.0);
        assertInstanceOf(OptionType.Barrier.class, opt);
    }

    @Test
    void testBinaryConstruction() {
        OptionType opt = new OptionType.Binary(BinaryType.CASH_OR_NOTHING);
        assertInstanceOf(OptionType.Binary.class, opt);
    }

    @Test
    void testLookbackConstruction() {
        OptionType opt = new OptionType.Lookback(LookbackType.FIXED_STRIKE);
        assertInstanceOf(OptionType.Lookback.class, opt);
    }

    @Test
    void testCompoundConstruction() {
        OptionType opt = new OptionType.Compound(new OptionType.European());
        assertInstanceOf(OptionType.Compound.class, opt);
    }

    @Test
    void testChooserConstruction() {
        OptionType opt = new OptionType.Chooser(30.0);
        assertInstanceOf(OptionType.Chooser.class, opt);
    }

    @Test
    void testCliquetConstruction() {
        OptionType opt = new OptionType.Cliquet(List.of(30.0, 60.0));
        assertInstanceOf(OptionType.Cliquet.class, opt);
    }

    @Test
    void testRainbowConstruction() {
        OptionType opt = new OptionType.Rainbow(RainbowType.BEST_OF, 2);
        assertInstanceOf(OptionType.Rainbow.class, opt);
    }

    @Test
    void testSpreadConstruction() {
        OptionType opt = new OptionType.Spread(90.0);
        assertInstanceOf(OptionType.Spread.class, opt);
    }

    @Test
    void testQuantoConstruction() {
        OptionType opt = new OptionType.Quanto(1.5);
        assertInstanceOf(OptionType.Quanto.class, opt);
    }

    @Test
    void testExchangeConstruction() {
        OptionType opt = new OptionType.Exchange(110.0);
        assertInstanceOf(OptionType.Exchange.class, opt);
    }

    @Test
    void testPowerConstruction() {
        OptionType opt = new OptionType.Power(2.0);
        assertInstanceOf(OptionType.Power.class, opt);
    }

    // --- isEuropean / isAmerican / isExotic ---

    @Test
    void testIsEuropean() {
        assertTrue(new OptionType.European().isEuropean());
        assertFalse(new OptionType.American().isEuropean());
        assertFalse(new OptionType.Power(2.0).isEuropean());
    }

    @Test
    void testIsAmerican() {
        assertTrue(new OptionType.American().isAmerican());
        assertFalse(new OptionType.European().isAmerican());
    }

    @Test
    void testIsExotic() {
        assertFalse(new OptionType.European().isExotic());
        assertFalse(new OptionType.American().isExotic());
        assertTrue(new OptionType.Asian(AsianAveragingType.ARITHMETIC).isExotic());
        assertTrue(new OptionType.Power(2.0).isExotic());
        assertTrue(new OptionType.Bermuda(List.of(30.0)).isExotic());
        assertTrue(new OptionType.Compound(new OptionType.European()).isExotic());
    }

    // --- isPathDependent ---

    @Test
    void testIsPathDependent() {
        assertTrue(new OptionType.Asian(AsianAveragingType.ARITHMETIC).isPathDependent());
        assertTrue(new OptionType.Barrier(BarrierType.UP_AND_IN, 120.0, null).isPathDependent());
        assertTrue(new OptionType.Lookback(LookbackType.FIXED_STRIKE).isPathDependent());
        assertTrue(new OptionType.Cliquet(List.of(30.0)).isPathDependent());
        assertFalse(new OptionType.European().isPathDependent());
        assertFalse(new OptionType.Power(2.0).isPathDependent());
    }

    // --- isMultiAsset ---

    @Test
    void testIsMultiAsset() {
        assertTrue(new OptionType.Rainbow(RainbowType.BEST_OF, 2).isMultiAsset());
        assertTrue(new OptionType.Spread(90.0).isMultiAsset());
        assertTrue(new OptionType.Exchange(110.0).isMultiAsset());
        assertFalse(new OptionType.European().isMultiAsset());
        assertFalse(new OptionType.Quanto(1.5).isMultiAsset());
    }

    // --- Barrier knock-in/out/up/down ---

    @Test
    void testBarrierKnockInOut() {
        OptionType knockIn  = new OptionType.Barrier(BarrierType.UP_AND_IN,   120.0, null);
        OptionType knockOut = new OptionType.Barrier(BarrierType.DOWN_AND_OUT, 80.0, null);

        assertTrue(knockIn.isKnockIn());
        assertFalse(knockIn.isKnockOut());
        assertTrue(knockOut.isKnockOut());
        assertFalse(knockOut.isKnockIn());
    }

    @Test
    void testBarrierTypeHelpers() {
        assertTrue(BarrierType.UP_AND_IN.isKnockIn());
        assertTrue(BarrierType.DOWN_AND_IN.isKnockIn());
        assertFalse(BarrierType.UP_AND_OUT.isKnockIn());
        assertTrue(BarrierType.UP_AND_IN.isUp());
        assertTrue(BarrierType.UP_AND_OUT.isUp());
        assertFalse(BarrierType.DOWN_AND_IN.isUp());
        assertTrue(BarrierType.DOWN_AND_IN.isDown());
        assertTrue(BarrierType.DOWN_AND_OUT.isDown());
    }

    // --- displayName ---

    @Test
    void testDisplayNames() {
        assertEquals("European Option", new OptionType.European().displayName());
        assertEquals("American Option", new OptionType.American().displayName());
        assertTrue(new OptionType.Bermuda(List.of(30.0)).displayName().contains("Bermuda"));
        assertTrue(new OptionType.Asian(AsianAveragingType.ARITHMETIC).displayName().contains("Arithmetic"));
        assertTrue(new OptionType.Barrier(BarrierType.UP_AND_IN, 120.0, 5.0).displayName().contains("120"));
        assertTrue(new OptionType.Binary(BinaryType.CASH_OR_NOTHING).displayName().contains("Binary"));
        assertTrue(new OptionType.Lookback(LookbackType.FIXED_STRIKE).displayName().contains("Lookback"));
        assertTrue(new OptionType.Chooser(30.0).displayName().contains("Chooser"));
        assertTrue(new OptionType.Cliquet(List.of(30.0)).displayName().contains("Cliquet"));
        assertTrue(new OptionType.Rainbow(RainbowType.BEST_OF, 3).displayName().contains("Rainbow"));
        assertTrue(new OptionType.Spread(90.0).displayName().contains("Spread"));
        assertTrue(new OptionType.Quanto(1.5).displayName().contains("Quanto"));
        assertTrue(new OptionType.Exchange(110.0).displayName().contains("Exchange"));
        assertTrue(new OptionType.Power(2.0).displayName().contains("Power"));
        assertTrue(new OptionType.Compound(new OptionType.European()).displayName().contains("Compound"));
    }

    // --- defaultValue ---

    @Test
    void testDefaultValue() {
        OptionType def = OptionType.defaultValue();
        assertTrue(def.isEuropean());
        assertInstanceOf(OptionType.European.class, def);
    }

    // --- Jackson JSON round-trip ---

    @Test
    void testJsonRoundTripEuropean() throws Exception {
        OptionType opt = new OptionType.European();
        String json = mapper.writeValueAsString(opt);
        OptionType result = mapper.readValue(json, OptionType.class);
        assertEquals(opt, result);
    }

    @Test
    void testJsonRoundTripAmerican() throws Exception {
        OptionType opt = new OptionType.American();
        String json = mapper.writeValueAsString(opt);
        OptionType result = mapper.readValue(json, OptionType.class);
        assertEquals(opt, result);
    }

    @Test
    void testJsonRoundTripAsian() throws Exception {
        OptionType opt = new OptionType.Asian(AsianAveragingType.GEOMETRIC);
        String json = mapper.writeValueAsString(opt);
        OptionType result = mapper.readValue(json, OptionType.class);
        assertEquals(opt, result);
    }

    @Test
    void testJsonRoundTripBermuda() throws Exception {
        OptionType opt = new OptionType.Bermuda(List.of(30.0, 60.0));
        String json = mapper.writeValueAsString(opt);
        OptionType result = mapper.readValue(json, OptionType.class);
        assertEquals(opt, result);
    }

    @Test
    void testJsonRoundTripBinary() throws Exception {
        OptionType opt = new OptionType.Binary(BinaryType.GAP);
        String json = mapper.writeValueAsString(opt);
        OptionType result = mapper.readValue(json, OptionType.class);
        assertEquals(opt, result);
    }

    @Test
    void testJsonRoundTripLookback() throws Exception {
        OptionType opt = new OptionType.Lookback(LookbackType.FLOATING_STRIKE);
        String json = mapper.writeValueAsString(opt);
        OptionType result = mapper.readValue(json, OptionType.class);
        assertEquals(opt, result);
    }

    @Test
    void testJsonRoundTripChooser() throws Exception {
        OptionType opt = new OptionType.Chooser(30.0);
        String json = mapper.writeValueAsString(opt);
        OptionType result = mapper.readValue(json, OptionType.class);
        assertEquals(opt, result);
    }

    @Test
    void testJsonRoundTripCliquet() throws Exception {
        OptionType opt = new OptionType.Cliquet(List.of(30.0, 60.0));
        String json = mapper.writeValueAsString(opt);
        OptionType result = mapper.readValue(json, OptionType.class);
        assertEquals(opt, result);
    }

    @Test
    void testJsonRoundTripRainbow() throws Exception {
        OptionType opt = new OptionType.Rainbow(RainbowType.WORST_OF, 2);
        String json = mapper.writeValueAsString(opt);
        OptionType result = mapper.readValue(json, OptionType.class);
        assertEquals(opt, result);
    }

    @Test
    void testJsonRoundTripSpread() throws Exception {
        OptionType opt = new OptionType.Spread(90.0);
        String json = mapper.writeValueAsString(opt);
        OptionType result = mapper.readValue(json, OptionType.class);
        assertEquals(opt, result);
    }

    @Test
    void testJsonRoundTripQuanto() throws Exception {
        OptionType opt = new OptionType.Quanto(1.5);
        String json = mapper.writeValueAsString(opt);
        OptionType result = mapper.readValue(json, OptionType.class);
        assertEquals(opt, result);
    }

    @Test
    void testJsonRoundTripExchange() throws Exception {
        OptionType opt = new OptionType.Exchange(110.0);
        String json = mapper.writeValueAsString(opt);
        OptionType result = mapper.readValue(json, OptionType.class);
        assertEquals(opt, result);
    }

    @Test
    void testJsonRoundTripPower() throws Exception {
        OptionType opt = new OptionType.Power(2.0);
        String json = mapper.writeValueAsString(opt);
        OptionType result = mapper.readValue(json, OptionType.class);
        assertEquals(opt, result);
    }

    // --- Compound wrapping ---

    @Test
    void testCompoundWrappingEuropean() {
        OptionType.Compound compound = new OptionType.Compound(new OptionType.European());
        assertTrue(compound.underlyingOption().isEuropean());
        assertTrue(compound.isExotic());
        assertFalse(compound.isEuropean());
        assertTrue(compound.displayName().contains("Compound"));
        assertTrue(compound.displayName().contains("European"));
    }

    // --- OptionBasicType record ---

    @Test
    void testOptionBasicTypeEquality() {
        OptionBasicType a = new OptionBasicType(Side.CALL, OptionStyle.EUROPEAN);
        OptionBasicType b = new OptionBasicType(Side.CALL, OptionStyle.EUROPEAN);
        assertEquals(a, b);
        assertTrue(a.isCall());
        assertFalse(a.isPut());
    }

    // --- Sub-enum display strings ---

    @Test
    void testAsianAveragingTypeDisplay() {
        assertEquals("Arithmetic Averaging", AsianAveragingType.ARITHMETIC.toString());
        assertEquals("Geometric Averaging",  AsianAveragingType.GEOMETRIC.toString());
        assertTrue(AsianAveragingType.ARITHMETIC.isArithmetic());
        assertTrue(AsianAveragingType.GEOMETRIC.isGeometric());
    }

    @Test
    void testBinaryTypeDisplay() {
        assertEquals("Cash-Or-Nothing Binary Option",  BinaryType.CASH_OR_NOTHING.toString());
        assertEquals("Asset-Or-Nothing Binary Option", BinaryType.ASSET_OR_NOTHING.toString());
        assertEquals("Gap Binary Option",              BinaryType.GAP.toString());
    }

    @Test
    void testLookbackTypeDisplay() {
        assertEquals("Fixed-Strike Lookback Option",    LookbackType.FIXED_STRIKE.toString());
        assertEquals("Floating-Strike Lookback Option", LookbackType.FLOATING_STRIKE.toString());
    }

    @Test
    void testRainbowTypeHelpers() {
        assertTrue(RainbowType.BEST_OF.isBestOf());
        assertFalse(RainbowType.BEST_OF.isWorstOf());
        assertTrue(RainbowType.WORST_OF.isWorstOf());
    }

    // --- Barrier JSON round-trip with rebate ---

    @Test
    void testJsonRoundTripBarrierWithRebate() throws Exception {
        OptionType opt = new OptionType.Barrier(BarrierType.DOWN_AND_OUT, 90.0, 2.5);
        String json = mapper.writeValueAsString(opt);
        OptionType result = mapper.readValue(json, OptionType.class);
        assertEquals(opt, result);
    }

    @Test
    void testJsonRoundTripBarrierNoRebate() throws Exception {
        OptionType opt = new OptionType.Barrier(BarrierType.UP_AND_OUT, 130.0, null);
        String json = mapper.writeValueAsString(opt);
        OptionType result = mapper.readValue(json, OptionType.class);
        assertEquals(opt, result);
    }
}
