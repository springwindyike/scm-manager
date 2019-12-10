import React, { ChangeEvent, KeyboardEvent } from "react";
import LabelWithHelpIcon from "./LabelWithHelpIcon";

type Props = {
  name?: string;
  label?: string;
  placeholder?: string;
  value?: string;
  autofocus?: boolean;
  onChange: (value: string, name?: string) => void;
  helpText?: string;
  disabled?: boolean;
  onSubmit?: () => void;
};

class Textarea extends React.Component<Props> {
  field: HTMLTextAreaElement | null | undefined;

  componentDidMount() {
    if (this.props.autofocus && this.field) {
      this.field.focus();
    }
  }

  handleInput = (event: ChangeEvent<HTMLTextAreaElement>) => {
    this.props.onChange(event.target.value, this.props.name);
  };

  onKeyPress = (event: KeyboardEvent<HTMLTextAreaElement>) => {
    const { onSubmit } = this.props;
    if (onSubmit && event.key === "Enter" && event.ctrlKey) {
      onSubmit();
    }
  };

  render() {
    const { placeholder, value, label, helpText, disabled } = this.props;

    return (
      <div className="field">
        <LabelWithHelpIcon label={label} helpText={helpText} />
        <div className="control">
          <textarea
            className="textarea"
            ref={input => {
              this.field = input;
            }}
            placeholder={placeholder}
            onChange={this.handleInput}
            value={value}
            disabled={!!disabled}
            onKeyPress={this.onKeyPress}
          />
        </div>
      </div>
    );
  }
}

export default Textarea;
