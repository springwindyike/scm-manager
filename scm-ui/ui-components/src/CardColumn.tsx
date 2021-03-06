/*
 * MIT License
 *
 * Copyright (c) 2020-present Cloudogu GmbH and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import React, { ReactNode } from "react";
import classNames from "classnames";
import styled from "styled-components";
import { Link } from "react-router-dom";

type Props = {
  title: ReactNode;
  description?: string;
  avatar: ReactNode;
  contentRight?: ReactNode;
  footerLeft: ReactNode;
  footerRight: ReactNode;
  link?: string;
  action?: () => void;
  className?: string;
};

const NoEventWrapper = styled.article`
  position: relative;
  pointer-events: none;
  z-index: 1;
`;

const AvatarWrapper = styled.figure`
  margin-top: 0.8em;
  margin-left: 1em !important;
`;

const FlexFullHeight = styled.div`
  flex-direction: column;
  justify-content: space-around;
  align-self: stretch;
`;

const FooterWrapper = styled.div`
  padding-bottom: 1rem;
`;

const ContentLeft = styled.div`
  margin-bottom: 0 !important;
  overflow: hidden;
`;

const ContentRight = styled.div`
  margin-left: auto;
`;

export default class CardColumn extends React.Component<Props> {
  createLink = () => {
    const { link, action } = this.props;
    if (link) {
      return <Link className="overlay-column" to={link} />;
    } else if (action) {
      return (
        <a
          className="overlay-column"
          onClick={e => {
            e.preventDefault();
            action();
          }}
          href="#"
        />
      );
    }
    return null;
  };

  render() {
    const { avatar, title, description, contentRight, footerLeft, footerRight, className } = this.props;
    const link = this.createLink();
    return (
      <>
        {link}
        <NoEventWrapper className={classNames("media", className)}>
          <AvatarWrapper className="media-left">{avatar}</AvatarWrapper>
          <FlexFullHeight className={classNames("media-content", "text-box", "is-flex")}>
            <div className="is-flex">
              <ContentLeft className="content">
                <p className="shorten-text is-marginless">
                  {title}
                </p>
                <p className="shorten-text">{description}</p>
              </ContentLeft>
              <ContentRight>{contentRight}</ContentRight>
            </div>
            <FooterWrapper className={classNames("level", "is-flex")}>
              <div className="level-left is-hidden-mobile">{footerLeft}</div>
              <div className="level-right is-mobile is-marginless">{footerRight}</div>
            </FooterWrapper>
          </FlexFullHeight>
        </NoEventWrapper>
      </>
    );
  }
}
