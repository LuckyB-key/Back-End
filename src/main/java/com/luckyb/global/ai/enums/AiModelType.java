package com.luckyb.global.ai.enums;

/**
 * AI 모델 타입 enum
 */
public enum AiModelType {
  GPT_3_5_TURBO("gpt-3.5-turbo", "GPT-3.5 Turbo"),
  GPT_4("gpt-4", "GPT-4"),
  CLAUDE_3("claude-3", "Claude 3");

  private final String modelName;
  private final String displayName;

  AiModelType(String modelName, String displayName) {
    this.modelName = modelName;
    this.displayName = displayName;
  }

  public String getModelName() {
    return modelName;
  }

  public String getDisplayName() {
    return displayName;
  }
} 