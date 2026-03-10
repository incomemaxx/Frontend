import CategoryBar from "../Category/CategoryBar";
import MaxLogo from "./MaxLogo";
import SearchBar from "./SearchBar";
import UserAuth from "./UserAuth";

export default function Header() {
  return (
    <header className="bg-[#000000] items-center border-b border-[#ffffff]/20">
      <div className="flex md:grid md:grid-cols-3 justify-between items-center max-w-330 mx-auto py-3 px-6">
        <MaxLogo />

        <div className="hidden md:block">
          <SearchBar />
        </div>

        <UserAuth />
      </div>
      <CategoryBar />
    </header>
  );
}
