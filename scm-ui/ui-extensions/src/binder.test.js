// @flow
import { Binder } from "./binder";

describe("binder tests", () => {
  let binder;

  beforeEach(() => {
    binder = new Binder();
  });

  it("should return an empty array for non existing extension points", () => {
    const extensions = binder.getExtensions("hitchhiker");
    expect(extensions).toEqual([]);
  });

  it("should return the binded extensions", () => {
    binder.bind("hitchhicker.trillian", "heartOfGold");
    binder.bind("hitchhicker.trillian", "earth");

    const extensions = binder.getExtensions("hitchhicker.trillian");
    expect(extensions).toEqual(["heartOfGold", "earth"]);
  });

  it("should return the first bound extension", () => {
    binder.bind("hitchhicker.trillian", "heartOfGold");
    binder.bind("hitchhicker.trillian", "earth");

    expect(binder.getExtension("hitchhicker.trillian")).toBe("heartOfGold");
  });

  it("should return null if no extension was bound", () => {
    expect(binder.getExtension("hitchhicker.trillian")).toBe(null);
  });

  it("should return true, if an extension is bound", () => {
    binder.bind("hitchhicker.trillian", "heartOfGold");
    expect(binder.hasExtension("hitchhicker.trillian")).toBe(true);
  });

  it("should return false, if no extension is bound", () => {
    expect(binder.hasExtension("hitchhicker.trillian")).toBe(false);
  });

  it("should return only extensions which predicates matches", () => {
    binder.bind("hitchhicker.trillian", "heartOfGold", (props: Object) => props.category === "a");
    binder.bind("hitchhicker.trillian", "earth", (props: Object) => props.category === "b");
    binder.bind("hitchhicker.trillian", "earth2", (props: Object) => props.category === "a");

    const extensions = binder.getExtensions("hitchhicker.trillian", { category: "b" });
    expect(extensions).toEqual(["earth"]);
  });
});
