import {assertEquals} from "./deps.ts";

Deno.test("simple test", () => {
    const x = 1 + 2;
    assertEquals(x, 3);
});
