package com.qsoftware.forgemod.script.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.qsoftware.forgemod.util.TextUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings({"SameParameterValue", "BooleanMethodIsAlwaysInverted", "unused", "deprecation"})
@OnlyIn(Dist.CLIENT)
public class ScriptJSTextWidget extends Widget implements IRenderable, IGuiEventListener {
   private final FontRenderer fontRenderer;
   /** Has the current text being edited on the textbox. */
   private String text = "";
   private int maxStringLength = 32;
   private int cursorCounter;
   private boolean enableBackgroundDrawing = true;
   /** if true the textbox can lose focus by clicking elsewhere on the screen */
   private boolean canLoseFocus = true;
   /** If this value is true along with isFocused, keyTyped will process the keys. */
   private boolean isEnabled = true;
   private boolean field_212956_h;
   /** The current character index that should be used as start of the rendered text. */
   private int lineScrollOffset;
   private int cursorPosition;
   /** other selection position, maybe the same as the cursor */
   private int selectionEnd;
   private int enabledColor = 14737632;
   private int disabledColor = 7368816;
   private String suggestion;
   private Consumer<String> guiResponder;
   /** Called to check if the text is valid */
   private Predicate<String> validator = Objects::nonNull;
   private BiFunction<String, Integer, IReorderingProcessor> textFormatter = (p_195610_0_, p_195610_1_) -> IReorderingProcessor.fromString(p_195610_0_, Style.EMPTY);

   public ScriptJSTextWidget(FontRenderer p_i232260_1_, int p_i232260_2_, int p_i232260_3_, int p_i232260_4_, int p_i232260_5_, ITextComponent p_i232260_6_) {
      this(p_i232260_1_, p_i232260_2_, p_i232260_3_, p_i232260_4_, p_i232260_5_, null, p_i232260_6_);
   }

   public ScriptJSTextWidget(FontRenderer p_i232259_1_, int p_i232259_2_, int p_i232259_3_, int p_i232259_4_, int p_i232259_5_, @Nullable ScriptJSTextWidget p_i232259_6_, ITextComponent p_i232259_7_) {
      super(p_i232259_2_, p_i232259_3_, p_i232259_4_, p_i232259_5_, p_i232259_7_);
      this.fontRenderer = p_i232259_1_;
      if (p_i232259_6_ != null) {
         this.setText(p_i232259_6_.getText());
      }

   }

   public void setResponder(Consumer<String> responderIn) {
      this.guiResponder = responderIn;
   }

   public void setTextFormatter(BiFunction<String, Integer, IReorderingProcessor> textFormatterIn) {
      this.textFormatter = textFormatterIn;
   }

   /**
    * Increments the cursor counter
    */
   public void tick() {
      ++this.cursorCounter;
   }

   protected IFormattableTextComponent getNarrationMessage() {
      ITextComponent itextcomponent = this.getMessage();
      return new TranslationTextComponent("gui.narrate.editBox", itextcomponent, this.text);
   }

   /**
    * Sets the text of the textbox, and moves the cursor to the end.
    */
   public void setText(String textIn) {
      if (this.validator.test(textIn)) {
         if (textIn.length() > this.maxStringLength) {
            this.text = textIn.substring(0, this.maxStringLength);
         } else {
            this.text = textIn;
         }

         this.setCursorPositionEnd();
         this.setSelectionPos(this.cursorPosition);
         this.onTextChanged(textIn);
      }
   }

   /**
    * Returns the contents of the textbox
    */
   public String getText() {
      return this.text;
   }

   /**
    * returns the text between the cursor and selectionEnd
    */
   public String getSelectedText() {
      int i = Math.min(this.cursorPosition, this.selectionEnd);
      int j = Math.max(this.cursorPosition, this.selectionEnd);
      return this.text.substring(i, j);
   }

   public void setValidator(Predicate<String> validatorIn) {
      this.validator = validatorIn;
   }

   /**
    * Adds the given text after the cursor, or replaces the currently selected text if there is a selection.
    */
   public void writeText(String textToWrite) {
      int i = Math.min(this.cursorPosition, this.selectionEnd);
      int j = Math.max(this.cursorPosition, this.selectionEnd);
      int k = this.maxStringLength - TextUtils.stripFormatting(this.text).length() - (i - j);
      String s = SharedConstants.filterAllowedCharacters(textToWrite);
      int l = TextUtils.stripFormatting(s).length();
      if (k < l) {
         s = s.substring(0, k);
         l = k;
      }

      String s1 = (new StringBuilder(this.text)).replace(i, j, s).toString();
      if (this.validator.test(s1)) {
         this.text = s1;
         this.clampCursorPosition(i);
         this.setSelectionPos(this.cursorPosition);
         this.onTextChanged(this.text);
      }
   }

   private void onTextChanged(String newText) {
      if (this.guiResponder != null) {
         this.guiResponder.accept(newText);
      }

      this.nextNarration = Util.milliTime() + 500L;
   }

   private void delete(int p_212950_1_) {
      if (Screen.hasControlDown()) {
         this.deleteWords(p_212950_1_);
      } else {
         this.deleteFromCursor(p_212950_1_);
      }

   }

   /**
    * Deletes the given number of words from the current cursor's position, unless there is currently a selection, in
    * which case the selection is deleted instead.
    */
   public void deleteWords(int num) {
      if (!this.text.isEmpty()) {
         if (this.selectionEnd != this.cursorPosition) {
            this.writeText("");
         } else {
            this.deleteFromCursor(this.getNthWordFromCursor(num) - this.cursorPosition);
         }
      }
   }

   /**
    * Deletes the given number of characters from the current cursor's position, unless there is currently a selection,
    * in which case the selection is deleted instead.
    */
   public void deleteFromCursor(int num) {
      if (!this.text.isEmpty()) {
         if (this.selectionEnd != this.cursorPosition) {
            this.writeText("");
         } else {
            int i = this.func_238516_r_(num);
            int j = Math.min(i, this.cursorPosition);
            int k = Math.max(i, this.cursorPosition);
            if (j != k) {
               String s = (new StringBuilder(this.text)).delete(j, k).toString();
               if (this.validator.test(s)) {
                  this.text = s;
                  this.setCursorPosition(j);
               }
            }
         }
      }
   }

   /**
    * Gets the starting index of the word at the specified number of words away from the cursor position.
    */
   public int getNthWordFromCursor(int numWords) {
      return this.getNthWordFromPos(numWords, this.getCursorPosition());
   }

   /**
    * Gets the starting index of the word at a distance of the specified number of words away from the given position.
    */
   private int getNthWordFromPos(int n, int pos) {
      return this.getNthWordFromPosWS(n, pos, true);
   }

   /**
    * Like getNthWordFromPos (which wraps this), but adds option for skipping consecutive spaces
    */
   private int getNthWordFromPosWS(int n, int pos, boolean skipWs) {
      int i = pos;
      boolean flag = n < 0;
      int j = Math.abs(n);

      for(int k = 0; k < j; ++k) {
         if (!flag) {
            int l = this.text.length();
            i = this.text.indexOf(32, i);
            if (i == -1) {
               i = l;
            } else {
               while(skipWs && i < l && this.text.charAt(i) == ' ') {
                  ++i;
               }
            }
         } else {
            while(skipWs && i > 0 && this.text.charAt(i - 1) == ' ') {
               --i;
            }

            while(i > 0 && this.text.charAt(i - 1) != ' ') {
               --i;
            }
         }
      }

      return i;
   }

   /**
    * Moves the text cursor by a specified number of characters and clears the selection
    */
   public void moveCursorBy(int num) {
      this.setCursorPosition(this.func_238516_r_(num));
   }

   private int func_238516_r_(int p_238516_1_) {
      return Util.func_240980_a_(this.text, this.cursorPosition, p_238516_1_);
   }

   /**
    * Sets the current position of the cursor.
    */
   public void setCursorPosition(int pos) {
      this.clampCursorPosition(pos);
      if (!this.field_212956_h) {
         this.setSelectionPos(this.cursorPosition);
      }

      this.onTextChanged(this.text);
   }

   public void clampCursorPosition(int pos) {
      this.cursorPosition = MathHelper.clamp(pos, 0, this.text.length());
   }

   /**
    * Moves the cursor to the very start of this text box.
    */
   public void setCursorPositionZero() {
      this.setCursorPosition(0);
   }

   /**
    * Moves the cursor to the very end of this text box.
    */
   public void setCursorPositionEnd() {
      this.setCursorPosition(this.text.length());
   }

   /**
    * ! The modification was made here! The changes are that around this.delete(...) methods the color formatting for QFM ScriptJS is cleaned, after the method the color will be reformatted.
    */
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (!this.canWrite()) {
         return false;
      } else {
         this.field_212956_h = Screen.hasShiftDown();
         if (Screen.isSelectAll(keyCode)) {
            this.setCursorPositionEnd();
            this.setSelectionPos(0);
            return true;
         } else if (Screen.isCopy(keyCode)) {
            Minecraft.getInstance().keyboardListener.setClipboardString(this.getSelectedText());
            return true;
         } else if (Screen.isPaste(keyCode)) {
            if (this.isEnabled) {
               this.writeText(Minecraft.getInstance().keyboardListener.getClipboardString());
            }

            return true;
         } else if (Screen.isCut(keyCode)) {
            Minecraft.getInstance().keyboardListener.setClipboardString(this.getSelectedText());
            if (this.isEnabled) {
               this.writeText("");
            }

            return true;
         } else {
            switch(keyCode) {
            case 259:
               if (this.isEnabled) {
                  this.field_212956_h = false;
//                  this.setText(TextUtils.stripFormatting(this.text));
                  this.delete(-1);
//                  this.setText(ScriptJSFormatter.instance().format(this.text));
                  this.field_212956_h = Screen.hasShiftDown();
               }

               return true;
            case 260:
            case 264:
            case 265:
            case 266:
            case 267:
            default:
               return false;
            case 261:
               if (this.isEnabled) {
                  this.field_212956_h = false;
//                  this.setText(TextUtils.stripFormatting(this.text));
                  this.delete(1);
//                  this.setText(ScriptJSFormatter.instance().format(this.text));
                  this.field_212956_h = Screen.hasShiftDown();
               }

               return true;
            case 262:
               if (Screen.hasControlDown()) {
                  this.setCursorPosition(this.getNthWordFromCursor(1));
               } else {
                  this.moveCursorBy(1);
               }

               return true;
            case 263:
               if (Screen.hasControlDown()) {
                  this.setCursorPosition(this.getNthWordFromCursor(-1));
               } else {
                  this.moveCursorBy(-1);
               }

               return true;
            case 268:
               this.setCursorPositionZero();
               return true;
            case 269:
               this.setCursorPositionEnd();
               return true;
            }
         }
      }
   }

   public boolean canWrite() {
      return this.getVisible() && this.isFocused() && this.isEnabled();
   }

   public boolean charTyped(char codePoint, int modifiers) {
      if (!this.canWrite()) {
         return false;
      } else if (SharedConstants.isAllowedCharacter(codePoint)) {
         if (this.isEnabled) {
//            this.setText(TextUtils.stripFormatting(this.text));
            this.writeText(Character.toString(codePoint));
//            this.setText(ScriptJSFormatter.instance().format(this.text));
            cursorPosition++;
            selectionEnd++;
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (!this.getVisible()) {
         return false;
      } else {
         boolean flag = mouseX >= (double)this.x && mouseX < (double)(this.x + this.width) && mouseY >= (double)this.y && mouseY < (double)(this.y + this.height);
         if (this.canLoseFocus) {
            this.setFocused2(flag);
         }

         if (this.isFocused() && flag && button == 0) {
            int i = MathHelper.floor(mouseX) - this.x;
            if (this.enableBackgroundDrawing) {
               i -= 4;
            }

            String s = this.fontRenderer.func_238412_a_(this.text.substring(this.lineScrollOffset), this.getAdjustedWidth());
            this.setCursorPosition(this.fontRenderer.func_238412_a_(s, i).length() + this.lineScrollOffset);
            return true;
         } else {
            return false;
         }
      }
   }

   /**
    * Sets focus to this gui element
    */
   public void setFocused2(boolean isFocusedIn) {
      super.setFocused(isFocusedIn);
   }

   public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
      if (this.getVisible()) {
         if (this.getEnableBackgroundDrawing()) {
            int color = this.isFocused() ? -1 : -6250336;
            fill(matrixStack, this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, color);
            fill(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height, 0xff000000);
         }

         int textColor = this.isEnabled ? this.enabledColor : this.disabledColor;
         int realCurPos = this.cursorPosition - this.lineScrollOffset;
         int realSelEnd = this.selectionEnd - this.lineScrollOffset;
         String text = this.text.substring(this.lineScrollOffset);
         boolean flag = realCurPos >= 0 && realCurPos <= text.length();
         boolean enableCursor = this.isFocused() && this.cursorCounter / 6 % 2 == 0 && flag;
         int deltaX = this.enableBackgroundDrawing ? this.x + 4 : this.x;
         int deltaY = this.enableBackgroundDrawing ? this.y + (this.height - 8) / 2 : this.y;
         int deltaXOld = deltaX;
         if (realSelEnd > text.length()) {
            realSelEnd = text.length();
         }

         if (!text.isEmpty()) {
            String s1 = flag ? text.substring(0, realCurPos) : text;
            deltaXOld = this.fontRenderer.drawStringWithShadow(matrixStack, s1, (float)deltaX, (float)deltaY, textColor);
         }

         boolean suggestionFlag = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
         int finalDeltaX = deltaXOld;
         if (!flag) {
            finalDeltaX = realCurPos > 0 ? deltaX + this.width : deltaX;
         } else if (suggestionFlag) {
            finalDeltaX = deltaXOld - 1;
            --deltaXOld;
         }

         if (!text.isEmpty() && flag && realCurPos < text.length()) {
            this.fontRenderer.drawStringWithShadow(matrixStack, text.substring(realCurPos), (float)deltaXOld, (float)deltaY, textColor);
         }

         if (!suggestionFlag && this.suggestion != null) {
            this.fontRenderer.drawStringWithShadow(matrixStack, this.suggestion, (float)(finalDeltaX - 1), (float)deltaY, -8355712);
         }

         if (enableCursor) {
            if (suggestionFlag) {
               AbstractGui.fill(matrixStack, finalDeltaX, deltaY - 1, finalDeltaX + 1, deltaY + 1 + 9, -3092272);
            } else {
               this.fontRenderer.drawStringWithShadow(matrixStack, "_", (float)finalDeltaX, (float)deltaY, textColor);
            }
         }

         if (realSelEnd != realCurPos) {
            int l1 = deltaX + this.fontRenderer.getStringWidth(text.substring(0, realSelEnd));
            this.drawSelectionBox(finalDeltaX, deltaY - 1, l1 - 1, deltaY + 1 + 9);
         }

      }
   }

   /**
    * Draws the blue selection box.
    */
   public void drawSelectionBox(int startX, int startY, int endX, int endY) {
      if (startX < endX) {
         int i = startX;
         startX = endX;
         endX = i;
      }

      if (startY < endY) {
         int j = startY;
         startY = endY;
         endY = j;
      }

      if (endX > this.x + this.width) {
         endX = this.x + this.width;
      }

      if (startX > this.x + this.width) {
         startX = this.x + this.width;
      }

      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      RenderSystem.color4f(0.0F, 0.0F, 255.0F, 255.0F);
      RenderSystem.disableTexture();
      RenderSystem.enableColorLogicOp();
      RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
      bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
      bufferbuilder.pos(startX, endY, 0.0D).endVertex();
      bufferbuilder.pos(endX, endY, 0.0D).endVertex();
      bufferbuilder.pos(endX, startY, 0.0D).endVertex();
      bufferbuilder.pos(startX, startY, 0.0D).endVertex();
      tessellator.draw();
      RenderSystem.disableColorLogicOp();
      RenderSystem.enableTexture();
   }

   /**
    * Sets the maximum length for the text in this text box. If the current text is longer than this length, the current
    * text will be trimmed.
    */
   public void setMaxStringLength(int length) {
      this.maxStringLength = length;
      if (this.text.length() > length) {
         this.text = this.text.substring(0, length);
         this.onTextChanged(this.text);
      }

   }

   /**
    * returns the maximum number of character that can be contained in this textbox
    */
   public int getMaxStringLength() {
      return this.maxStringLength;
   }

   /**
    * returns the current position of the cursor
    */
   public int getCursorPosition() {
      return this.cursorPosition;
   }

   /**
    * Gets whether the background and outline of this text box should be drawn (true if so).
    */
   protected boolean getEnableBackgroundDrawing() {
      return this.enableBackgroundDrawing;
   }

   /**
    * Sets whether or not the background and outline of this text box should be drawn.
    */
   public void setEnableBackgroundDrawing(boolean enableBackgroundDrawingIn) {
      this.enableBackgroundDrawing = enableBackgroundDrawingIn;
   }

   /**
    * Sets the color to use when drawing this text box's text. A different color is used if this text box is disabled.
    */
   public void setTextColor(int color) {
      this.enabledColor = color;
   }

   /**
    * Sets the color to use for text in this text box when this text box is disabled.
    */
   public void setDisabledTextColour(int color) {
      this.disabledColor = color;
   }

   public boolean changeFocus(boolean focus) {
      return this.visible && this.isEnabled && super.changeFocus(focus);
   }

   public boolean isMouseOver(double mouseX, double mouseY) {
      return this.visible && mouseX >= (double)this.x && mouseX < (double)(this.x + this.width) && mouseY >= (double)this.y && mouseY < (double)(this.y + this.height);
   }

   protected void onFocusedChanged(boolean focused) {
      if (focused) {
         this.cursorCounter = 0;
      }

   }

   private boolean isEnabled() {
      return this.isEnabled;
   }

   /**
    * Sets whether this text box is enabled. Disabled text boxes cannot be typed in.
    */
   public void setEnabled(boolean enabled) {
      this.isEnabled = enabled;
   }

   /**
    * returns the width of the textbox depending on if background drawing is enabled
    */
   public int getAdjustedWidth() {
      return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
   }

   /**
    * Sets the position of the selection anchor (the selection anchor and the cursor position mark the edges of the
    * selection). If the anchor is set beyond the bounds of the current text, it will be put back inside.
    */
   public void setSelectionPos(int position) {
      int i = this.text.length();
      this.selectionEnd = MathHelper.clamp(position, 0, i);
      if (this.fontRenderer != null) {
         if (this.lineScrollOffset > i) {
            this.lineScrollOffset = i;
         }

         int j = this.getAdjustedWidth();
         String s = this.fontRenderer.func_238412_a_(this.text.substring(this.lineScrollOffset), j);
         int k = s.length() + this.lineScrollOffset;
         if (this.selectionEnd == this.lineScrollOffset) {
            this.lineScrollOffset -= this.fontRenderer.func_238413_a_(this.text, j, true).length();
         }

         if (this.selectionEnd > k) {
            this.lineScrollOffset += this.selectionEnd - k;
         } else if (this.selectionEnd <= this.lineScrollOffset) {
            this.lineScrollOffset -= this.lineScrollOffset - this.selectionEnd;
         }

         this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, i);

         System.out.println("Char-1: " + (this.lineScrollOffset - 1 >= 0 && this.lineScrollOffset - 1 < this.text.length() ? this.text.charAt(this.lineScrollOffset-1) : ""));
         System.out.println("Char  : " + (this.lineScrollOffset     >= 0 && this.lineScrollOffset     < this.text.length() ? this.text.charAt(this.lineScrollOffset  ) : ""));
         System.out.println("Char+1: " + (this.lineScrollOffset + 1 >= 0 && this.lineScrollOffset + 1 < this.text.length() ? this.text.charAt(this.lineScrollOffset+1) : ""));
         if (this.lineScrollOffset - 1 >= 0 && this.lineScrollOffset - 1 < this.text.length() && this.text.charAt(this.lineScrollOffset - 1) == '\u00A7') {
            this.lineScrollOffset++;
         }
      }

   }

   /**
    * Sets whether this text box loses focus when something other than it is clicked.
    */
   public void setCanLoseFocus(boolean canLoseFocusIn) {
      this.canLoseFocus = canLoseFocusIn;
   }

   /**
    * returns true if this textbox is visible
    */
   public boolean getVisible() {
      return this.visible;
   }

   /**
    * Sets whether or not this textbox is visible
    */
   public void setVisible(boolean isVisible) {
      this.visible = isVisible;
   }

   public void setSuggestion(@Nullable String p_195612_1_) {
      this.suggestion = p_195612_1_;
   }

   public int func_195611_j(int p_195611_1_) {
      return p_195611_1_ > this.text.length() ? this.x : this.x + this.fontRenderer.getStringWidth(this.text.substring(0, p_195611_1_));
   }

   public void setX(int xIn) {
      this.x = xIn;
   }
}